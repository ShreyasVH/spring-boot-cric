package pack.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pack.models.db.*;
import pack.models.requests.matches.CreateRequest;
import pack.models.responses.BattingScoreResponse;
import pack.models.responses.MatchResponse;
import pack.repositories.*;
import pack.services.MatchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private BattingScoreRepository battingScoreRepository;
    @Autowired
    private BowlerDismissalRepository bowlerDismissalRepository;
    @Autowired
    private BowlingFigureRepository bowlingFigureRepository;
    @Autowired
    private ExtrasRepository extrasRepository;
    @Autowired
    private FielderDismissalRepository fielderDismissalRepository;
    @Autowired
    private ManOfTheMatchRepository manOfTheMatchRepository;
    @Autowired
    private ManOfTheSeriesRepository manOfTheSeriesRepository;
    @Autowired
    private CaptainRepository captainRepository;
    @Autowired
    private WicketKeeperRepository wicketKeeperRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private DismissalModeRepository dismissalModeRepository;
    @Autowired
    private MatchPlayerMapRepository matchPlayerMapRepository;
    @Autowired
    private ExtrasTypeRepository extrasTypeRepository;
    @Autowired
    private ResultTypeRepository resultTypeRepository;
    @Autowired
    private WinMarginTypeRepository winMarginTypeRepository;

    public MatchResponse matchResponse(Match match)
    {
        MatchResponse matchResponse = new MatchResponse(match);
        List<BattingScoreResponse> battingScoreResponses = battingScoreRepository.findAllByMatchId(match.getId()).stream().map(BattingScoreResponse::new).collect(Collectors.toList());
        List<Long> bowlerDismissalIds = battingScoreResponses.stream().filter(battingScoreResponse -> battingScoreResponse.getBowlerDismissalId() != null).map(BattingScoreResponse::getBowlerDismissalId).collect(Collectors.toList());
        List<BowlerDismissal> bowlerDismissals = bowlerDismissalRepository.findAllById(bowlerDismissalIds);
        Map<Long, BowlerDismissal> bowlerDismissalMap = bowlerDismissals.stream().collect(Collectors.toMap(BowlerDismissal::getId, bowlerDismissal -> bowlerDismissal));
        battingScoreResponses = battingScoreResponses.stream().peek(battingScoreResponse -> {
            if((battingScoreResponse.getBowlerDismissalId() != null) && bowlerDismissalMap.containsKey(battingScoreResponse.getBowlerDismissalId()))
            {
                battingScoreResponse.setBowler(bowlerDismissalMap.get(battingScoreResponse.getBowlerDismissalId()));
            }
        }).collect(Collectors.toList());

        List<Long> scoreIds = battingScoreResponses.stream().map(BattingScoreResponse::getId).collect(Collectors.toList());
        List<FielderDismissal> fielderDismissals = fielderDismissalRepository.findAllByScoreIdIn(scoreIds);
        Map<Long, List<FielderDismissal>> fielderDismissalMap = new HashMap<>();
        fielderDismissals.forEach(fielderDismissal -> {
            List<FielderDismissal> dismissals = new ArrayList<>();
            if(fielderDismissalMap.containsKey(fielderDismissal.getScoreId()))
            {
                dismissals = fielderDismissalMap.get(fielderDismissal.getScoreId());
            }
            dismissals.add(fielderDismissal);
            fielderDismissalMap.put(fielderDismissal.getScoreId(), dismissals);
        });
        battingScoreResponses = battingScoreResponses.stream().peek(battingScoreResponse -> {
            if(fielderDismissalMap.containsKey(battingScoreResponse.getId()))
            {
                battingScoreResponse.setFielders(fielderDismissalMap.get(battingScoreResponse.getId()));
            }
        }).collect(Collectors.toList());

        matchResponse.setBattingScores(battingScoreResponses);

        matchResponse.setBowlingFigures(bowlingFigureRepository.findAllByMatchId(matchResponse.getId()));
        matchResponse.setExtras(extrasRepository.findAllByMatchId(matchResponse.getId()));
        matchResponse.setPlayers(matchPlayerMapRepository.findAllByMatchId(matchResponse.getId()));
        matchResponse.setManOfTheMatchList(manOfTheMatchRepository.findAllByMatchId(match.getId()));
        matchResponse.setCaptains(captainRepository.findAllByMatchId(match.getId()));
        matchResponse.setWicketKeepers(wicketKeeperRepository.findAllByMatchId(match.getId()));

        return matchResponse;
    }

    @Transactional
    @Override
    public Match create(CreateRequest createRequest) {
        Match match = new Match(createRequest);
//        match.setResultTypeId(resultTypeRepository.findByName(createRequest.getResult()).getId());
//        match.setWinMarginTypeId(winMarginTypeRepository.findByName(createRequest.getWinMarginType()).getId());
        Match createdMatch = matchRepository.save(match);

        Map<Long, Player> playerIdPlayerMap = new HashMap<>();
        Map<Long, Team> playerIdTeamMap = new HashMap<>();

        if(null != createRequest.getPlayers())
        {
            List<MatchPlayerMap> matchPlayerMaps = new ArrayList<>();
            for (Map<String, String> matchPlayerMapRaw : createRequest.getPlayers())
            {
                MatchPlayerMap matchPlayerMap = new MatchPlayerMap();
                Long playerId = Long.parseLong(matchPlayerMapRaw.get("playerId"));
                Long teamId = Long.parseLong(matchPlayerMapRaw.get("teamId"));

                Player player = playerRepository.findById(playerId).orElse(null);
                if (null == player)
                {
                    String sh = "sh";
//                    throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Player"));
                }

                Team team = this.teamRepository.findById(teamId).orElse(null);
                if (null == team)
                {
                    String sh = "sh";
//                    throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Player's Team"));
                }

                matchPlayerMap.setMatchId(createdMatch.getId());
                matchPlayerMap.setTeamId(teamId);
                matchPlayerMap.setPlayerId(playerId);

                playerIdPlayerMap.put(playerId, player);
                playerIdTeamMap.put(playerId, team);

                matchPlayerMaps.add(matchPlayerMap);
            }
            this.matchPlayerMapRepository.saveAll(matchPlayerMaps);

            if (null != createRequest.getBench())
            {
                for (Map<String, String> matchPlayerMapRaw : createRequest.getBench())
                {
                    Long playerId = Long.parseLong(matchPlayerMapRaw.get("playerId"));
                    Long teamId = Long.parseLong(matchPlayerMapRaw.get("teamId"));
                    Player player = playerRepository.findById(playerId).orElse(null);
                    if (null == player)
                    {
                        String sh = "sh";
//                        throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Player"));
                    }

                    Team team = teamRepository.findById(teamId).orElse(null);
                    if (null == team)
                    {
                        String sh = "sh";
//                        throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Player's Team"));
                    }

                    playerIdPlayerMap.put(playerId, player);
                    playerIdTeamMap.put(playerId, team);
                }
            }
        }

        List<Extras> extras = new ArrayList<>();
        for(Map<String, String> extraRaw: createRequest.getExtras())
        {
            try
            {
                Long battingTeamId = Long.parseLong(extraRaw.get("battingTeam"));
                Long bowlingTeamId = Long.parseLong(extraRaw.get("bowlingTeam"));
                Extras extra = new Extras();
                extra.setRuns(Integer.parseInt(extraRaw.get("runs")));
                extra.setType(pack.enums.ExtrasType.valueOf(extraRaw.get("type")));
//                extra.setTypeId(extrasTypeRepository.findByName(extraRaw.get("type")).getId());
                extra.setMatchId(createdMatch.getId());
                Team battingTeam = teamRepository.findById(battingTeamId).orElse(null);
                if(null == battingTeam)
                {
                    String sh = "sh";
//                    throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Batting Team"));
                }
                extra.setBattingTeam(battingTeam.getId());
                Team bowlingTeam = teamRepository.findById(bowlingTeamId).orElse(null);
                if(null == bowlingTeam)
                {
                    String sh = "sh";
//                    throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Bowling Team"));
                }
                extra.setBowlingTeam(bowlingTeam.getId());
                extra.setInnings(Integer.parseInt(extraRaw.get("innings")));
                extra.setTeamInnings(Integer.parseInt(extraRaw.get("teamInnings")));
                extras.add(extra);
            }
            catch(Exception ex)
            {
                throw ex;
//                throw new BadRequestException(ErrorCode.INVALID_REQUEST.getCode(), ErrorCode.INVALID_REQUEST.getDescription());
            }
        }
        extrasRepository.saveAll(extras);

        List<BowlingFigure> bowlingFigures = new ArrayList<>();
        for(Map<String, String> bowlingFigureRaw: createRequest.getBowlingFigures())
        {
            BowlingFigure bowlingFigure = new BowlingFigure();
            bowlingFigure.setMatchId(createdMatch.getId());

            Player player = playerIdPlayerMap.get(Long.parseLong(bowlingFigureRaw.get("playerId")));
            if(null == player)
            {
                String sh = "sh";
//                throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Bowler"));
            }
            bowlingFigure.setPlayerId(player.getId());

            Long playerId = Long.parseLong(bowlingFigureRaw.get("playerId"));
            Team team = playerIdTeamMap.get(playerId);
            if(null == team)
            {
                String sh = "sh";
//                throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Bowler's Team"));
            }
            bowlingFigure.setTeamId(team.getId());

            bowlingFigure.setBalls(Integer.parseInt(bowlingFigureRaw.get("balls")));
            bowlingFigure.setMaidens(Integer.parseInt(bowlingFigureRaw.get("maidens")));
            bowlingFigure.setRuns(Integer.parseInt(bowlingFigureRaw.get("runs")));
            bowlingFigure.setWickets(Integer.parseInt(bowlingFigureRaw.get("wickets")));
            bowlingFigure.setInnings(Integer.parseInt(bowlingFigureRaw.get("innings")));
            bowlingFigure.setTeamInnings(Integer.parseInt(bowlingFigureRaw.get("teamInnings")));

            bowlingFigures.add(bowlingFigure);
        }
        bowlingFigureRepository.saveAll(bowlingFigures);

        List<ManOfTheMatch> manOfTheMatchList = new ArrayList<>();
        List<Long> motmPlayerIds = new ArrayList<>();
        for(Long playerId: createRequest.getManOfTheMatchList())
        {
            if(!motmPlayerIds.contains(playerId))
            {
                ManOfTheMatch manOfTheMatch = new ManOfTheMatch();
                manOfTheMatch.setMatchId(createdMatch.getId());
                manOfTheMatch.setPlayerId(playerId);
                manOfTheMatch.setTeamId(playerIdTeamMap.get(playerId).getId());

                manOfTheMatchList.add(manOfTheMatch);
                motmPlayerIds.add(playerId);
            }
        }
        manOfTheMatchRepository.saveAll(manOfTheMatchList);

        for(Map<String, String> battingScoreRaw: createRequest.getBattingScores())
        {
            boolean isBowlerPresent = false;
            BowlerDismissal bowlerDismissal = new BowlerDismissal();
            if(null != battingScoreRaw.get("bowlerId") && StringUtils.hasText(battingScoreRaw.get("bowlerId")))
            {
                Long bowlerId = Long.parseLong(battingScoreRaw.get("bowlerId"));
                bowlerDismissal.setPlayerId(bowlerId);
                bowlerDismissal.setTeamId(playerIdTeamMap.get(bowlerId).getId());
                bowlerDismissalRepository.save(bowlerDismissal);
                isBowlerPresent = true;
            }


            BattingScore battingScore = new BattingScore();

            battingScore.setMatchId(createdMatch.getId());
            if(isBowlerPresent)
            {
                battingScore.setBowlerDismissalId(bowlerDismissal.getId());
            }
            battingScore.setPlayerId(Long.parseLong(battingScoreRaw.get("playerId")));
            Long battingTeamId = playerIdTeamMap.get(Long.parseLong(battingScoreRaw.get("playerId"))).getId();
            Long bowlingTeamId = ((battingTeamId.equals(createRequest.getTeam1())) ? createRequest.getTeam2() : createRequest.getTeam1());
            battingScore.setTeamId(battingTeamId);
            battingScore.setRuns(Integer.parseInt(battingScoreRaw.get("runs")));
            battingScore.setBalls(Integer.parseInt(battingScoreRaw.get("balls")));
            battingScore.setFours(Integer.parseInt(battingScoreRaw.get("fours")));
            battingScore.setSixes(Integer.parseInt(battingScoreRaw.get("sixes")));

            if(null != battingScoreRaw.get("dismissalMode") && StringUtils.hasText(battingScoreRaw.get("dismissalMode")))
            {
                Integer dismissalModeId = Integer.parseInt(battingScoreRaw.get("dismissalMode"));
                DismissalMode dismissalMode = dismissalModeRepository.findById(dismissalModeId).orElse(null);
                if(null == dismissalMode)
                {
                    String sh = "sh";
//                    throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Dismissal Mode"));
                }

                battingScore.setDismissalMode(dismissalModeId);
            }
            battingScore.setInnings(Integer.parseInt(battingScoreRaw.get("innings")));
            battingScore.setTeamInnings(Integer.parseInt(battingScoreRaw.get("teamInnings")));

            battingScoreRepository.save(battingScore);

            if(null != battingScoreRaw.get("fielders") && StringUtils.hasText(battingScoreRaw.get("fielders")))
            {
                String[] fielderIds = battingScoreRaw.get("fielders").split(", ");
                List<FielderDismissal> fielders = new ArrayList<>();
                for(String fielderIdString: fielderIds)
                {
                    Long fielderId = Long.parseLong(fielderIdString);
                    FielderDismissal fielderDismissal = new FielderDismissal();
                    fielderDismissal.setScoreId(battingScore.getId());
                    fielderDismissal.setPlayerId(fielderId);
                    fielderDismissal.setTeamId(bowlingTeamId);

                    fielders.add(fielderDismissal);
                }

                fielderDismissalRepository.saveAll(fielders);
            }
        }

        List<Captain> captains = new ArrayList<>();
        List<Long> processedCaptains = new ArrayList<>();
        for(Long playerId: createRequest.getCaptains())
        {
            Team team = playerIdTeamMap.get(playerId);
            if(!processedCaptains.contains(playerId) && (null != team))
            {
                Captain captain = new Captain();
                captain.setMatchId(createdMatch.getId());
                captain.setPlayerId(playerId);
                captain.setTeamId(team.getId());

                captains.add(captain);

                processedCaptains.add(playerId);
            }
        }
        captainRepository.saveAll(captains);

        List<WicketKeeper> wicketKeepers = new ArrayList<>();
        List<Long> processedWicketKeepers = new ArrayList<>();
        for(Long playerId: createRequest.getWicketKeepers())
        {
            Team team = playerIdTeamMap.get(playerId);
            if(!processedWicketKeepers.contains(playerId) && (null != team))
            {
                WicketKeeper wicketKeeper = new WicketKeeper();
                wicketKeeper.setMatchId(createdMatch.getId());
                wicketKeeper.setPlayerId(playerId);
                wicketKeeper.setTeamId(team.getId());

                wicketKeepers.add(wicketKeeper);

                processedWicketKeepers.add(playerId);
            }
        }
        wicketKeeperRepository.saveAll(wicketKeepers);

        return createdMatch;
    }

    @Override
    public MatchResponse get(Long id) {
        Match match = matchRepository.findById(id).orElse(null);

        return matchResponse(match);
    }


}
