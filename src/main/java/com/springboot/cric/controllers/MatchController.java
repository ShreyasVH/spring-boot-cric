package com.springboot.cric.controllers;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.*;
import com.springboot.cric.requests.matches.PlayerRequest;
import com.springboot.cric.requests.matches.CreateRequest;
import com.springboot.cric.responses.*;
import com.springboot.cric.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MatchController {
    @Autowired
    private MatchService matchService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamTypeService teamTypeService;
    @Autowired
    private ResultTypeService resultTypeService;
    @Autowired
    private WinMarginTypeService winMarginTypeService;
    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private MatchPlayerMapService matchPlayerMapService;
    @Autowired
    private BattingScoreService battingScoreService;
    @Autowired
    private DismissalModeService dismissalModeService;
    @Autowired
    private FielderDismissalService fielderDismissalService;
    @Autowired
    private BowlingFigureService bowlingFigureService;
    @Autowired
    private ExtrasService extrasService;
    @Autowired
    private ExtrasTypeService extrasTypeService;
    @Autowired
    private ManOfTheMatchService manOfTheMatchService;
    @Autowired
    private CaptainService captainService;
    @Autowired
    private WicketKeeperService wicketKeeperService;
    @Autowired
    private GameTypeService gameTypeService;

    @Transactional
    @PostMapping("/cric/v1/matches")
    public ResponseEntity<Response> create(@RequestBody CreateRequest createRequest)
    {
        Series series = seriesService.getById(createRequest.getSeriesId());
        if(null == series)
        {
            throw new NotFoundException("Series");
        }

        GameType gameType = gameTypeService.getById(series.getGameTypeId());

        List<Long> countryIds = new ArrayList<>();

        List<Long> teamIds = new ArrayList<>();
        teamIds.add(createRequest.getTeam1Id());
        teamIds.add(createRequest.getTeam2Id());
        List<Team> teams = teamService.getByIds(teamIds);
        Map<Long, Team> teamMap = new HashMap<>();
        for(Team team: teams)
        {
            teamMap.put(team.getId(), team);
            countryIds.add(team.getCountryId());
        }

        Team team1 = teamMap.get(createRequest.getTeam1Id());
        if(null == team1)
        {
            throw new NotFoundException("Team 1");
        }

        Team team2 = teamMap.get(createRequest.getTeam2Id());
        if(null == team2)
        {
            throw new NotFoundException("Team 2");
        }

        ResultType resultType = resultTypeService.getById(createRequest.getResultTypeId());
        if(null == resultType)
        {
            throw new NotFoundException("Result type");
        }

        WinMarginTypeResponse winMarginTypeResponse = null;
        if(null != createRequest.getWinMarginTypeId())
        {
            WinMarginType winMarginType = winMarginTypeService.getById(createRequest.getWinMarginTypeId());
            if(null == winMarginType)
            {
                throw new NotFoundException("Win margin type");
            }
            winMarginTypeResponse = new WinMarginTypeResponse(winMarginType);
        }

        Stadium stadium = stadiumService.getById(createRequest.getStadiumId());
        if(null == stadium)
        {
            throw new NotFoundException("Stadium");
        }

        Map<Long, Long> playerTeamMap = new HashMap<>();
        List<Long> allPlayerIds = new ArrayList<>();
        for(PlayerRequest playerRequest: createRequest.getPlayers())
        {
            playerTeamMap.put(playerRequest.getId(), playerRequest.getTeamId());
            allPlayerIds.add(playerRequest.getId());
        }
        for(PlayerRequest playerRequest: createRequest.getBench())
        {
            playerTeamMap.put(playerRequest.getId(), playerRequest.getTeamId());
            allPlayerIds.add(playerRequest.getId());
        }

        List<Player> allPlayers = playerService.getByIds(allPlayerIds);
        List<Long> playerCountryIds = allPlayers.stream().map(Player::getCountryId).collect(Collectors.toList());
        Map<Long, Player> playerMap = allPlayers.stream().collect(Collectors.toMap(Player::getId, player -> player));

        countryIds.add(team1.getCountryId());
        countryIds.add(team2.getCountryId());
        countryIds.add(stadium.getCountryId());
        countryIds.addAll(playerCountryIds);
        List<Integer> teamTypeIds = Arrays.asList(team1.getTypeId(), team2.getTypeId());
        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        List<BattingScoreResponse> battingScoreResponses = new ArrayList<>();
        List<BowlingFigureResponse> bowlingFigureResponses = new ArrayList<>();
        List<ExtrasResponse> extrasResponses = new ArrayList<>();
        Match match = matchService.create(createRequest);
        List<MatchPlayerMap> matchPlayerMapList = matchPlayerMapService.add(match.getId(), allPlayerIds, playerTeamMap);
        Map<Long, Integer> playerToMatchPlayerMap = matchPlayerMapList.stream().collect(Collectors.toMap(MatchPlayerMap::getPlayerId, MatchPlayerMap::getId));
        List<BattingScore> battingScores = battingScoreService.add(createRequest.getBattingScores(), playerToMatchPlayerMap);
        List<DismissalMode> dismissalModes = dismissalModeService.getAll();
        Map<Integer, DismissalMode> dismissalModeMap = dismissalModes.stream().collect(Collectors.toMap(DismissalMode::getId, dismissalMode -> dismissalMode));
        Map<String, BattingScore> battingScoreMap = battingScores.stream().collect(Collectors.toMap(battingScore -> battingScore.getMatchPlayerId() + "_" + battingScore.getInnings(), battingScore -> battingScore));
        Map<Integer, List<Long>> scoreFielderMap = new HashMap<>();
        battingScoreResponses = createRequest.getBattingScores().stream().map(battingScore -> {
            String key = playerToMatchPlayerMap.get(battingScore.getPlayerId()) + "_" + battingScore.getInnings();
            BattingScore battingScoreFromDb = battingScoreMap.get(key);

            DismissalModeResponse dismissalModeResponse = null;
            List<PlayerMiniResponse> fielders = new ArrayList<>();
            PlayerMiniResponse bowler = null;

            if(null != battingScore.getDismissalModeId())
            {
                dismissalModeResponse = new DismissalModeResponse(dismissalModeMap.get(battingScore.getDismissalModeId()));
                if(null != battingScore.getBowlerId())
                {
                    Player bowlerPlayer = playerMap.get(battingScore.getBowlerId());
                    bowler = new PlayerMiniResponse(bowlerPlayer, new CountryResponse(countryMap.get(bowlerPlayer.getCountryId())));
                }

                if(null != battingScore.getFielderIds())
                {
                    fielders = battingScore.getFielderIds().stream().map(playerId -> {
                        Player fielderPlayer = playerMap.get(playerId);
                        return new PlayerMiniResponse(fielderPlayer, new CountryResponse(countryMap.get(fielderPlayer.getCountryId())));
                    }).collect(Collectors.toList());
                    scoreFielderMap.put(battingScoreFromDb.getId(), battingScore.getFielderIds());
                }
            }
            Player batsmanPlayer = playerMap.get(battingScore.getPlayerId());

            return new BattingScoreResponse(
                    battingScoreFromDb,
                    new PlayerMiniResponse(batsmanPlayer, new CountryResponse(countryMap.get(batsmanPlayer.getCountryId()))),
                    dismissalModeResponse,
                    bowler,
                    fielders
            );
        }).collect(Collectors.toList());

        fielderDismissalService.add(scoreFielderMap, playerToMatchPlayerMap);
        List<BowlingFigure> bowlingFigures = bowlingFigureService.add(createRequest.getBowlingFigures(), playerToMatchPlayerMap);
        Map<String, BowlingFigure> bowlingFigureMap = bowlingFigures.stream().collect(Collectors.toMap(bowlingFigure -> bowlingFigure.getMatchPlayerId() + "_" + bowlingFigure.getInnings(), bowlingFigure -> bowlingFigure));
        bowlingFigureResponses = createRequest.getBowlingFigures().stream().map(bowlingFigureRequest -> {
            String key = playerToMatchPlayerMap.get(bowlingFigureRequest.getPlayerId()) + "_" + bowlingFigureRequest.getInnings();
            BowlingFigure bowlingFigure = bowlingFigureMap.get(key);

            Player bowlerPlayer = playerMap.get(bowlingFigureRequest.getPlayerId());

            return new BowlingFigureResponse(bowlingFigure, new PlayerMiniResponse(bowlerPlayer, new CountryResponse(countryMap.get(bowlerPlayer.getCountryId()))));
        }).collect(Collectors.toList());

        List<ExtrasType> extrasTypes = extrasTypeService.getAll();
        Map<Integer, ExtrasType> extrasTypeMap = extrasTypes.stream().collect(Collectors.toMap(ExtrasType::getId, extrasType -> extrasType));
        List<Extras> extrasList = extrasService.add(match.getId(), createRequest.getExtras());
        extrasResponses = extrasList.stream()
                .map(extras -> {
                    Team battingTeam = teamMap.get(extras.getBattingTeamId());
                    Team bowlingTeam = teamMap.get(extras.getBowlingTeamId());
                    return new ExtrasResponse(
                            extras,
                            new ExtrasTypeResponse(extrasTypeMap.get(extras.getTypeId())),
                            new TeamResponse(
                                    battingTeam,
                                    new CountryResponse(countryMap.get(battingTeam.getCountryId())),
                                    new TeamTypeResponse(teamTypeMap.get(battingTeam.getTypeId()))
                            ),
                            new TeamResponse(
                                    bowlingTeam,
                                    new CountryResponse(countryMap.get(bowlingTeam.getCountryId())),
                                    new TeamTypeResponse(teamTypeMap.get(bowlingTeam.getTypeId()))
                            )
                    );
                })
                .collect(Collectors.toList());

        manOfTheMatchService.add(createRequest.getManOfTheMatchList(), playerToMatchPlayerMap);
        captainService.add(createRequest.getCaptains(), playerToMatchPlayerMap);
        wicketKeeperService.add(createRequest.getWicketKeepers(), playerToMatchPlayerMap);

        Map<Long, List<PlayerMiniResponse>> teamPlayerMap = new HashMap<>();
        for(Player player: allPlayers)
        {
            PlayerMiniResponse playerMiniResponse = new PlayerMiniResponse(player, new CountryResponse(countryMap.get(player.getCountryId())));
            Long teamId = playerTeamMap.get(player.getId());
            if(!teamPlayerMap.containsKey(teamId))
            {
                teamPlayerMap.put(teamId, new ArrayList<>());
            }
            teamPlayerMap.get(teamId).add(playerMiniResponse);
        }

        MatchResponse matchResponse = new MatchResponse(
                match,
                series,
                gameType,
                new TeamResponse(team1, new CountryResponse(countryMap.get(team1.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team1.getTypeId()))),
                new TeamResponse(team2, new CountryResponse(countryMap.get(team2.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team2.getTypeId()))),
                new ResultTypeResponse(resultType),
                winMarginTypeResponse,
                new StadiumResponse(stadium, new CountryResponse(countryMap.get(stadium.getCountryId()))),
                teamPlayerMap,
                battingScoreResponses,
                bowlingFigureResponses,
                extrasResponses,
                createRequest.getManOfTheMatchList(),
                createRequest.getCaptains(),
                createRequest.getWicketKeepers()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(matchResponse));
    }

    @GetMapping("/cric/v1/matches/{id}")
    public ResponseEntity<Response> getById(@PathVariable Integer id)
    {
        Match match = matchService.getById(id);
        if(null == match)
        {
            throw new NotFoundException("Match");
        }

        Series series = seriesService.getById(match.getSeriesId());
        if(null == series)
        {
            throw new NotFoundException("Series");
        }

        GameType gameType = gameTypeService.getById(series.getGameTypeId());

        List<Long> countryIds = new ArrayList<>();

        List<Long> teamIds = new ArrayList<>();
        teamIds.add(match.getTeam1Id());
        teamIds.add(match.getTeam2Id());
        List<Team> teams = teamService.getByIds(teamIds);
        Map<Long, Team> teamMap = new HashMap<>();
        for(Team team: teams)
        {
            teamMap.put(team.getId(), team);
            countryIds.add(team.getCountryId());
        }

        Team team1 = teamMap.get(match.getTeam1Id());
        if(null == team1)
        {
            throw new NotFoundException("Team 1");
        }

        Team team2 = teamMap.get(match.getTeam2Id());
        if(null == team2)
        {
            throw new NotFoundException("Team 2");
        }

        ResultType resultType = resultTypeService.getById(match.getResultTypeId());
        if(null == resultType)
        {
            throw new NotFoundException("Result type");
        }

        WinMarginTypeResponse winMarginTypeResponse = null;
        if(null != match.getWinMarginTypeId())
        {
            WinMarginType winMarginType = winMarginTypeService.getById(match.getWinMarginTypeId());
            if(null == winMarginType)
            {
                throw new NotFoundException("Win margin type");
            }
            winMarginTypeResponse = new WinMarginTypeResponse(winMarginType);
        }

        Stadium stadium = stadiumService.getById(match.getStadiumId());
        if(null == stadium)
        {
            throw new NotFoundException("Stadium");
        }

        List<MatchPlayerMap> matchPlayerMaps = matchPlayerMapService.getByMatchId(id);
        List<Long> playerIds = new ArrayList<>();
        Map<Integer, Long> matchPlayerToPlayerMap = new HashMap<>();
        List<Integer> matchPlayerIds = new ArrayList<>();
        Map<Long, Long> playerToTeamMap = new HashMap<>();
        for(MatchPlayerMap matchPlayerMap: matchPlayerMaps)
        {
            playerIds.add(matchPlayerMap.getPlayerId());
            matchPlayerToPlayerMap.put(matchPlayerMap.getId(), matchPlayerMap.getPlayerId());
            matchPlayerIds.add(matchPlayerMap.getId());
            playerToTeamMap.put(matchPlayerMap.getPlayerId(), matchPlayerMap.getTeamId());
        }

        List<Player> players = playerService.getByIds(playerIds);
        List<Long> playerCountryIds = players.stream().map(Player::getCountryId).collect(Collectors.toList());

        countryIds.add(stadium.getCountryId());
        countryIds.addAll(playerCountryIds);
        List<Integer> teamTypeIds = Arrays.asList(team1.getTypeId(), team2.getTypeId());
        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        Map<Long, PlayerMiniResponse> playerMap = new HashMap<>();
        Map<Long, List<PlayerMiniResponse>> teamPlayerMap = new HashMap<>();
        for(Player player: players)
        {
            PlayerMiniResponse playerMiniResponse = new PlayerMiniResponse(player, new CountryResponse(countryMap.get(player.getCountryId())));
            playerMap.put(player.getId(), playerMiniResponse);
            Long teamId = playerToTeamMap.get(player.getId());
            if(!teamPlayerMap.containsKey(teamId))
            {
                teamPlayerMap.put(teamId, new ArrayList<>());
            }
            teamPlayerMap.get(teamId).add(playerMiniResponse);
        }

        List<ManOfTheMatch> manOfTheMatchList = manOfTheMatchService.getByMatchPlayerIds(matchPlayerIds);
        List<Captain> captains = captainService.getByMatchPlayerIds(matchPlayerIds);
        List<WicketKeeper> wicketKeepers = wicketKeeperService.getByMatchPlayerIds(matchPlayerIds);
        List<BattingScore> battingScores = battingScoreService.getBattingScores(matchPlayerIds);
        List<DismissalMode> dismissalModes = dismissalModeService.getAll();
        Map<Integer, DismissalMode> dismissalModeMap = dismissalModes.stream().collect(Collectors.toMap(DismissalMode::getId, dismissalMode -> dismissalMode));
        List<FielderDismissal> fielderDismissals = fielderDismissalService.get(matchPlayerIds);
        Map<Integer, List<FielderDismissal>> fielderDismissalMap = fielderDismissals.stream().collect(Collectors.groupingBy(FielderDismissal::getScoreId, Collectors.mapping(fielderDismissal -> fielderDismissal, Collectors.toList())));

        List<BattingScoreResponse> battingScoreResponses = new ArrayList<>();
        for(BattingScore battingScore: battingScores)
        {
            PlayerMiniResponse batsmanPlayer = playerMap.get(matchPlayerToPlayerMap.get(battingScore.getMatchPlayerId()));

            DismissalModeResponse dismissalModeResponse = null;
            if(null != battingScore.getDismissalModeId())
            {
                dismissalModeResponse = new DismissalModeResponse(dismissalModeMap.get(battingScore.getDismissalModeId()));
            }

            PlayerMiniResponse bowlerPlayer = null;
            if(null != battingScore.getBowlerId())
            {
                bowlerPlayer = playerMap.get(matchPlayerToPlayerMap.get(battingScore.getBowlerId()));
            }

            List<PlayerMiniResponse> fielders = new ArrayList<>();
            if(fielderDismissalMap.containsKey(battingScore.getId()))
            {
                List<FielderDismissal> fielderDismissalList = fielderDismissalMap.get(battingScore.getId());
                fielders = fielderDismissalList.stream().map(fd -> playerMap.get(matchPlayerToPlayerMap.get(fd.getMatchPlayerId()))).collect(Collectors.toList());
            }

            battingScoreResponses.add(new BattingScoreResponse(
                    battingScore,
                    batsmanPlayer,
                    dismissalModeResponse,
                    bowlerPlayer,
                    fielders
            ));
        }

        List<BowlingFigure> bowlingFigures = bowlingFigureService.get(matchPlayerIds);
        List<BowlingFigureResponse> bowlingFigureResponses = bowlingFigures.stream().map(bowlingFigure -> {
            PlayerMiniResponse bowlerPlayer = playerMap.get(matchPlayerToPlayerMap.get(bowlingFigure.getMatchPlayerId()));
            return new BowlingFigureResponse(bowlingFigure, bowlerPlayer);
        }).collect(Collectors.toList());

        List<ExtrasType> extrasTypes = extrasTypeService.getAll();
        Map<Integer, ExtrasType> extrasTypeMap = extrasTypes.stream().collect(Collectors.toMap(ExtrasType::getId, extrasType -> extrasType));
        List<Extras> extrasList = extrasService.getByMatchId(id);
        List<ExtrasResponse> extrasResponses = extrasList.stream().map(extras -> {
            ExtrasTypeResponse extrasTypeResponse = new ExtrasTypeResponse(extrasTypeMap.get(extras.getTypeId()));
            Team battingTeam = teamMap.get(extras.getBattingTeamId());
            Team bowlingTeam = teamMap.get(extras.getBowlingTeamId());
            return new ExtrasResponse(
                    extras,
                    extrasTypeResponse,
                    new TeamResponse(battingTeam, new CountryResponse(countryMap.get(battingTeam.getCountryId())), new TeamTypeResponse(teamTypeMap.get(battingTeam.getTypeId()))),
                    new TeamResponse(bowlingTeam, new CountryResponse(countryMap.get(bowlingTeam.getCountryId())), new TeamTypeResponse(teamTypeMap.get(bowlingTeam.getTypeId())))
            );
        }).collect(Collectors.toList());

        MatchResponse matchResponse = new MatchResponse(
                match,
                series,
                gameType,
                new TeamResponse(team1, new CountryResponse(countryMap.get(team1.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team1.getTypeId()))),
                new TeamResponse(team2, new CountryResponse(countryMap.get(team2.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team2.getTypeId()))),
                new ResultTypeResponse(resultType),
                winMarginTypeResponse,
                new StadiumResponse(stadium, new CountryResponse(countryMap.get(stadium.getCountryId()))),
                teamPlayerMap,
                battingScoreResponses,
                bowlingFigureResponses,
                extrasResponses,
                manOfTheMatchList.stream().map(motm -> matchPlayerToPlayerMap.get(motm.getMatchPlayerId())).collect(Collectors.toList()),
                captains.stream().map(captain -> matchPlayerToPlayerMap.get(captain.getMatchPlayerId())).collect(Collectors.toList()),
                wicketKeepers.stream().map(wicketKeeper -> matchPlayerToPlayerMap.get(wicketKeeper.getMatchPlayerId())).collect(Collectors.toList())
        );

        return ResponseEntity.status(HttpStatus.OK).body(new Response(matchResponse));
    }

    @Transactional
    @DeleteMapping("/cric/v1/matches/{id}")
    public ResponseEntity<Response> remove(@PathVariable Integer id)
    {
        Match match = matchService.getById(id);
        if(null == match)
        {
            throw new NotFoundException("Match");
        }

        List<MatchPlayerMap> matchPlayerMaps = matchPlayerMapService.getByMatchId(id);
        List<Integer> matchPlayerIds = matchPlayerMaps.stream().map(MatchPlayerMap::getId).collect(Collectors.toList());
        extrasService.remove(id);
        captainService.remove(matchPlayerIds);
        wicketKeeperService.remove(matchPlayerIds);
        manOfTheMatchService.remove(matchPlayerIds);
        fielderDismissalService.remove(matchPlayerIds);
        battingScoreService.remove(matchPlayerIds);
        bowlingFigureService.remove(matchPlayerIds);
        matchPlayerMapService.remove(id);
        matchService.remove(id);

        return ResponseEntity.ok(new Response("Deleted successfully"));
    }
}
