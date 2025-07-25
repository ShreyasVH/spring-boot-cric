package com.springboot.cric.controllers;

import com.springboot.cric.exceptions.BadRequestException;
import com.springboot.cric.requests.players.MergeRequest;
import com.springboot.cric.responses.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.springboot.cric.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.Country;
import com.springboot.cric.models.Player;
import com.springboot.cric.requests.players.CreateRequest;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private BattingScoreService battingScoreService;
    @Autowired
    private BowlingFigureService bowlingFigureService;
    @Autowired
    private FielderDismissalService fielderDismissalService;
    @Autowired
    private ManOfTheSeriesService manOfTheSeriesService;
    @Autowired
    private MatchPlayerMapService matchPlayerMapService;

    private List<PlayerMiniResponse> getPlayerResponses(List<Player> players)
    {
        List<Long> countryIds = players.stream().map(Player::getCountryId).collect(Collectors.toList());
        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        return players.stream().map(player -> new PlayerMiniResponse(player, new CountryResponse(countryMap.get(player.getCountryId())))).collect(Collectors.toList());
    }

    @PostMapping("/cric/v1/players")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        Country country = countryService.getById(request.getCountryId());
        if(null == country) {
            throw new NotFoundException("Country");
        }

        Player player = playerService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new PlayerMiniResponse(player, new CountryResponse(country))));
    }

    @GetMapping("/cric/v1/players")
    public ResponseEntity<Response> getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Player> players = playerService.getAll(page, limit);
        long totalCount = 0L;
        if(page == 1) {
            totalCount = playerService.getTotalCount();
        }

        List<PlayerMiniResponse> playerResponses = getPlayerResponses(players);
        PaginatedResponse<PlayerMiniResponse> paginatedResponse = new PaginatedResponse<>(totalCount, playerResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }

    @GetMapping("/cric/v1/players/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id)
    {
        Player player = playerService.getById(id);
        if(null == player)
        {
            throw new NotFoundException("Player");
        }

        PlayerResponse playerResponse = new PlayerResponse(player);
        Country country = countryService.getById(player.getCountryId());
        playerResponse.setCountry(new CountryResponse(country));

        Map<String, Map<String, Integer>> dismissalStats = battingScoreService.getDismissalStats(id);
        playerResponse.setDismissalStats(dismissalStats);

        Map<String, Integer> dismissalCountMap = new HashMap<>();
        for(String gameType: dismissalStats.keySet())
        {
            Integer dismissalCount = 0;
            for(String key: dismissalStats.get(gameType).keySet())
            {
                dismissalCount += dismissalStats.get(gameType).get(key);
            }
            dismissalCountMap.put(gameType, dismissalCount);
        }

        Map<String, Map<String, Integer>> basicBattingStats = battingScoreService.getBattingStats(id);
        if(!basicBattingStats.keySet().isEmpty())
        {
            Map<String, BattingStats> battingStatsMap = new HashMap<>();

            for(String gameType: basicBattingStats.keySet())
            {
                BattingStats battingStats = new BattingStats(basicBattingStats.get(gameType));
                battingStats.setNotOuts(battingStats.getInnings() - dismissalCountMap.getOrDefault(gameType, 0));

                if(dismissalCountMap.getOrDefault(gameType, 0) > 0)
                {
                    battingStats.setAverage(battingStats.getRuns() * 1.0 / dismissalCountMap.get(gameType));
                }

                if(battingStats.getBalls() > 0)
                {
                    battingStats.setStrikeRate(battingStats.getRuns() * 100.0 / battingStats.getBalls());
                }

                battingStatsMap.put(gameType, battingStats);
            }

            playerResponse.setBattingStats(battingStatsMap);
        }

        Map<String, Map<String, Integer>> basicBowlingStatsMap = bowlingFigureService.getBowlingStats(id);
        if(!basicBowlingStatsMap.keySet().isEmpty())
        {
            Map<String, BowlingStats> bowlingStatsFinal = new HashMap<>();

            for(String gameType: basicBowlingStatsMap.keySet())
            {
                BowlingStats bowlingStats = new BowlingStats(basicBowlingStatsMap.get(gameType));

                if(bowlingStats.getBalls() > 0)
                {
                    bowlingStats.setEconomy(bowlingStats.getRuns() * 6.0 / bowlingStats.getBalls());

                    if(bowlingStats.getWickets() > 0)
                    {
                        bowlingStats.setAverage(bowlingStats.getRuns() * 1.0 / bowlingStats.getWickets());

                        bowlingStats.setStrikeRate(bowlingStats.getBalls() * 1.0 / bowlingStats.getWickets());
                    }
                }

                bowlingStatsFinal.put(gameType, bowlingStats);
            }

            playerResponse.setBowlingStats(bowlingStatsFinal);
        }

        Map<String, Map<String, Integer>> fieldingStatsMap = fielderDismissalService.getFieldingStats(id);
        if(!fieldingStatsMap.keySet().isEmpty())
        {
            Map<String, FieldingStats> fieldingStatsMapFinal = new HashMap<>();
            for(String gameType: fieldingStatsMap.keySet())
            {
                FieldingStats fieldingStats = new FieldingStats(fieldingStatsMap.get(gameType));
                fieldingStatsMapFinal.put(gameType, fieldingStats);
            }

            playerResponse.setFieldingStats(fieldingStatsMapFinal);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Response(playerResponse));
    }

    @Transactional
    @PostMapping("/cric/v1/players/merge")
    public ResponseEntity<Response> merge(@RequestBody MergeRequest request)
    {
        if(request.getOriginalPlayerId().equals(request.getPlayerIdToMerge()))
        {
            throw new BadRequestException("Same player given");
        }

        Player player = playerService.getById(request.getPlayerIdToMerge());
        if(null == player)
        {
            throw new NotFoundException("Player");
        }

        Player originalPlayer = playerService.getById(request.getOriginalPlayerId());
        if(null == originalPlayer)
        {
            throw new NotFoundException("Original Player");
        }

        manOfTheSeriesService.merge(request);
        matchPlayerMapService.merge(request);
        playerService.remove(request.getPlayerIdToMerge());

        return ResponseEntity.status(HttpStatus.OK).body(new Response("Success"));
    }

    @GetMapping("/cric/v1/players/search")
    public ResponseEntity<Response> search(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Player> players = playerService.search(keyword, page, limit);

        long totalCount = 0L;
        if(page == 1) {
            totalCount = playerService.searchCount(keyword);
        }

        List<PlayerMiniResponse> playerResponses = getPlayerResponses(players);
        PaginatedResponse<PlayerMiniResponse> paginatedResponse = new PaginatedResponse<>(totalCount, playerResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }
}