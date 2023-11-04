package com.springboot.cric.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.cric.models.GameType;
import com.springboot.cric.models.Match;
import com.springboot.cric.models.Series;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MatchResponse {
    private Integer id;
    private SeriesMiniResponse series;
    private TeamResponse team1;
    private TeamResponse team2;
    private TeamResponse tossWinner;
    private TeamResponse batFirst;
    private ResultTypeResponse resultType;
    private TeamResponse winner;
    private Integer winMargin;
    private WinMarginTypeResponse winMarginType;
    private StadiumResponse stadium;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;
    private List<BattingScoreResponse> battingScores;
    private List<BowlingFigureResponse> bowlingFigures;
    private List<ExtrasResponse> extras;
    private Map<Long, List<PlayerMiniResponse>> players;
    private List<PlayerMiniResponse> manOfTheMatchList;
    private List<PlayerMiniResponse> captains;
    private List<PlayerMiniResponse> wicketKeepers;

    public MatchResponse(Match match, Series series, GameType gameType, TeamResponse team1, TeamResponse team2, ResultTypeResponse resultType, WinMarginTypeResponse winMarginType, StadiumResponse stadium, Map<Long, List<PlayerMiniResponse>> players, List<BattingScoreResponse> battingScores, List<BowlingFigureResponse> bowlingFigures, List<ExtrasResponse> extras, List<Long> manOfTheMatchPlayerIds, List<Long> captainIds, List<Long> wicketKeeperIds)
    {
        this.id = match.getId();
        this.series = new SeriesMiniResponse(series, gameType);
        this.team1 = team1;
        this.team2 = team2;
        Map<Long, TeamResponse> teamMap = Map.of(
                team1.getId(), team1,
                team2.getId(), team2
        );
        if(null != match.getTossWinnerId())
        {
            this.tossWinner = teamMap.get(match.getTossWinnerId());
            this.batFirst = teamMap.get(match.getBatFirstId());
        }
        if(null != match.getWinnerId())
        {
            this.winner = teamMap.get(match.getWinnerId());
            this.winMargin = match.getWinMargin();
            this.winMarginType = winMarginType;
        }
        this.resultType = resultType;
        this.stadium = stadium;
        this.startTime = match.getStartTime();
        this.battingScores = battingScores;
        this.bowlingFigures = bowlingFigures;
        this.extras = extras;
        this.players = players;
        Map<Long, PlayerMiniResponse> playerMap = new HashMap<>();
        for(Map.Entry<Long, List<PlayerMiniResponse>> entry: players.entrySet())
        {
            for (PlayerMiniResponse player: entry.getValue()) {
                playerMap.put(player.getId(), player);
            }
        }
        this.manOfTheMatchList = manOfTheMatchPlayerIds.stream().map(playerMap::get).collect(Collectors.toList());
        this.captains = captainIds.stream().map(playerMap::get).collect(Collectors.toList());
        this.wicketKeepers = wicketKeeperIds.stream().map(playerMap::get).collect(Collectors.toList());
    }
}
