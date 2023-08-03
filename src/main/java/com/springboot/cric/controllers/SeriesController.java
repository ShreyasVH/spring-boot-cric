package com.springboot.cric.controllers;

import com.springboot.cric.models.*;
import com.springboot.cric.requests.series.UpdateRequest;
import com.springboot.cric.responses.*;

import java.util.*;
import java.util.stream.Collectors;

import com.springboot.cric.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.requests.series.CreateRequest;

@RestController
public class SeriesController {
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private SeriesTypeService seriesTypeService;
    @Autowired
    private GameTypeService gameTypeService;
    @Autowired
    private TourService tourService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamTypeService teamTypeService;
    @Autowired
    private SeriesTeamsMapService seriesTeamsMapService;
    @Autowired
    private ManOfTheSeriesService manOfTheSeriesService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private ResultTypeService resultTypeService;
    @Autowired
    private WinMarginTypeService winMarginTypeService;
    @Autowired
    private StadiumService stadiumService;

    @PostMapping("/cric/v1/series")
    @Transactional
    public ResponseEntity<Response> create(@RequestBody CreateRequest createRequest)
    {
        List<Team> teams = teamService.getByIds(createRequest.getTeams());
        if(teams.size() != createRequest.getTeams().stream().distinct().count()) {
            throw new NotFoundException("Team");
        }

        List<Integer> teamTypeIds = new ArrayList<>();
        List<Long> countryIds = new ArrayList<>();
        for(Team team: teams) {
            teamTypeIds.add(team.getTypeId());
            countryIds.add(team.getCountryId());
        }

        countryIds.add(createRequest.getHomeCountryId());
        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        Country country = countryMap.get(createRequest.getHomeCountryId());
        if(null == country) {
            throw new NotFoundException("Home country");
        }

        Tour tour = tourService.getById(createRequest.getTourId());
        if(null == tour) {
            throw new NotFoundException("Tour");
        }

        SeriesType seriesType = seriesTypeService.getById(createRequest.getTypeId());
        if(null == seriesType) {
            throw new NotFoundException("Type");
        }

        GameType gameType = gameTypeService.getById(createRequest.getGameTypeId());
        if(null == gameType) {
            throw new NotFoundException("Game type");
        }

        Series series = seriesService.create(createRequest);
        seriesTeamsMapService.create(series.getId(), createRequest.getTeams());

        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<TeamResponse> teamResponses = teams.stream().map(team -> new TeamResponse(team, new CountryResponse(countryMap.get(team.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team.getTypeId())))).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new SeriesResponse(series, new CountryResponse(country), new TourMiniResponse(tour), new SeriesTypeResponse(seriesType), new GameTypeResponse(gameType), teamResponses, new ArrayList<>())));
    }

    @GetMapping("/cric/v1/series")
    public ResponseEntity<Response> getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Series> seriesList = seriesService.getAll(page, limit);
        long totalCount = 0;
        if(page == 1) {
            totalCount = seriesService.getTotalCount();
        }

        List<Long> countryIds = new ArrayList<>();
        List<Integer> seriesTypeIds = new ArrayList<>();
        List<Integer> gameTypeIds = new ArrayList<>();
        List<Long> tourIds = new ArrayList<>();
        List<Long> seriesIds = new ArrayList<>();

        for (Series series: seriesList) {
            countryIds.add(series.getHomeCountryId());
            seriesTypeIds.add(series.getTypeId());
            gameTypeIds.add(series.getGameTypeId());
            tourIds.add(series.getTourId());
            seriesIds.add(series.getId());
        }

        List<SeriesType> seriesTypes = seriesTypeService.getByIds(seriesTypeIds);
        Map<Integer, SeriesType> seriesTypeMap = seriesTypes.stream().collect(Collectors.toMap(SeriesType::getId, seriesType -> seriesType));

        List<GameType> gameTypes = gameTypeService.getByIds(gameTypeIds);
        Map<Integer, GameType> gameTypeMap = gameTypes.stream().collect(Collectors.toMap(GameType::getId, gameType -> gameType));

        List<SeriesTeamsMap> seriesTeamsMaps = seriesTeamsMapService.getBySeriesIds(seriesIds);
        List<Long> teamIds = seriesTeamsMaps.stream().map(SeriesTeamsMap::getTeamId).collect(Collectors.toList());

        List<Team> teams = teamService.getByIds(teamIds);

        List<Integer> teamTypeIds = new ArrayList<>();
        for (Team team: teams) {
            teamTypeIds.add(team.getTypeId());
            countryIds.add(team.getCountryId());
        }

        List<ManOfTheSeries> manOfTheSeriesList = manOfTheSeriesService.getBySeriesIds(seriesIds);
        List<Long> playerIds = manOfTheSeriesList.stream().map(ManOfTheSeries::getPlayerId).collect(Collectors.toList());
        List<Player> players = playerService.getByIds(playerIds);
        List<Long> playerCountryIds = players.stream().map(Player::getCountryId).collect(Collectors.toList());
        countryIds.addAll(playerCountryIds);

        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<Tour> tours = tourService.getByIds(tourIds);
        Map<Long, Tour> tourMap = tours.stream().collect(Collectors.toMap(Tour::getId, tour -> tour));

        List<TeamResponse> teamResponses = teams.stream().map(team -> new TeamResponse(team, new CountryResponse(countryMap.get(team.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team.getTypeId())))).collect(Collectors.toList());

        List<PlayerMiniResponse> playerResponses = players.stream().map(player -> new PlayerMiniResponse(player, new CountryResponse(countryMap.get(player.getCountryId())))).collect(Collectors.toList());

        List<SeriesResponse> seriesResponses = seriesList.stream().map(series -> new SeriesResponse(series, new CountryResponse(countryMap.get(series.getHomeCountryId())), new TourMiniResponse(tourMap.get(series.getTourId())), new SeriesTypeResponse(seriesTypeMap.get(series.getTypeId())), new GameTypeResponse(gameTypeMap.get(series.getGameTypeId())), teamResponses, playerResponses)).collect(Collectors.toList());

        PaginatedResponse<SeriesResponse> paginatedResponse = new PaginatedResponse<>(totalCount, seriesResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }

    @PutMapping("/cric/v1/series/{id}")
    @Transactional
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody UpdateRequest updateRequest) {
        Series existingSeries = seriesService.getById(id);
        if (null == existingSeries) {
            throw new NotFoundException("Series");
        }

        List<Long> teamsToDelete = new ArrayList<>();
        List<Long> teamsToAdd = new ArrayList<>();
        List<Long> manOfTheSeriesToDelete = new ArrayList<>();
        List<Long> manOfTheSeriesToAdd = new ArrayList<>();
        List<Team> teams;
        List<SeriesTeamsMap> seriesTeamsMaps = seriesTeamsMapService.getBySeriesIds(Collections.singletonList(id));
        List<Long> existingTeamIds = new ArrayList<>();
        for (SeriesTeamsMap seriesTeamsMap: seriesTeamsMaps) {
            existingTeamIds.add(seriesTeamsMap.getTeamId());
            if (null != updateRequest.getTeams() && !updateRequest.getTeams().contains(seriesTeamsMap.getTeamId())) {
                teamsToDelete.add(seriesTeamsMap.getTeamId());
            }
        }
        if (null != updateRequest.getTeams()) {
            teams = teamService.getByIds(updateRequest.getTeams());
            if(teams.size() != updateRequest.getTeams().stream().distinct().count()) {
                throw new NotFoundException("Team");
            }

            teamsToAdd = updateRequest.getTeams().stream().filter(teamId -> !existingTeamIds.contains(teamId)).collect(Collectors.toList());
        } else {
            teams = teamService.getByIds(existingTeamIds);
        }

        List<Integer> teamTypeIds = new ArrayList<>();
        List<Long> countryIds = new ArrayList<>();
        for(Team team: teams) {
            teamTypeIds.add(team.getTypeId());
            countryIds.add(team.getCountryId());
        }

        if(null != updateRequest.getHomeCountryId()) {
            countryIds.add(updateRequest.getHomeCountryId());
        } else {
            countryIds.add(existingSeries.getHomeCountryId());
        }

        List<Player> players;
        List<ManOfTheSeries> manOfTheSeriesList = manOfTheSeriesService.getBySeriesIds(Collections.singletonList(id));
        List<Long> existingPlayerIds = new ArrayList<>();
        for (ManOfTheSeries manOfTheSeries: manOfTheSeriesList) {
            existingPlayerIds.add(manOfTheSeries.getPlayerId());
            if (null != updateRequest.getManOfTheSeriesList() && !updateRequest.getManOfTheSeriesList().contains(manOfTheSeries.getPlayerId())) {
                manOfTheSeriesToDelete.add(manOfTheSeries.getPlayerId());
            }
        }
        if (null != updateRequest.getManOfTheSeriesList()) {
            players = playerService.getByIds(updateRequest.getManOfTheSeriesList());
            if(players.size() != updateRequest.getManOfTheSeriesList().stream().distinct().count()) {
                throw new NotFoundException("Player");
            }

            manOfTheSeriesToAdd = updateRequest.getManOfTheSeriesList().stream().filter(playerId -> !existingPlayerIds.contains(playerId)).collect(Collectors.toList());
        } else {
            players = playerService.getByIds(existingPlayerIds);
        }

        List<Long> playerCountryIds = players.stream().map(Player::getCountryId).collect(Collectors.toList());
        countryIds.addAll(playerCountryIds);

        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        Long homeCountryId;
        if (null != updateRequest.getHomeCountryId()) {
            homeCountryId = updateRequest.getHomeCountryId();
        } else {
            homeCountryId = existingSeries.getHomeCountryId();
        }
        Country country = countryMap.get(homeCountryId);
        if(null == country) {
            throw new NotFoundException("Home country");
        }

        Long tourId;
        if (null != updateRequest.getTourId()) {
            tourId = updateRequest.getTourId();
        } else {
            tourId = existingSeries.getTourId();
        }
        Tour tour = tourService.getById(tourId);
        if(null == tour) {
            throw new NotFoundException("Tour");
        }

        Integer seriesTypeId;
        if (null != updateRequest.getTypeId()) {
            seriesTypeId = updateRequest.getTypeId();
        } else {
            seriesTypeId = existingSeries.getTypeId();
        }
        SeriesType seriesType = seriesTypeService.getById(seriesTypeId);
        if(null == seriesType) {
            throw new NotFoundException("Type");
        }

        Integer gameTypeId;
        if (null != updateRequest.getGameTypeId()) {
            gameTypeId = updateRequest.getGameTypeId();
        } else {
            gameTypeId = existingSeries.getGameTypeId();
        }
        GameType gameType = gameTypeService.getById(gameTypeId);
        if(null == gameType) {
            throw new NotFoundException("Game type");
        }

        seriesService.update(existingSeries, updateRequest);
        seriesTeamsMapService.create(id, teamsToAdd);
        seriesTeamsMapService.delete(id, teamsToDelete);
        manOfTheSeriesService.add(id, manOfTheSeriesToAdd);
        manOfTheSeriesService.delete(id, manOfTheSeriesToDelete);

        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<TeamResponse> teamResponses = teams.stream().map(team -> new TeamResponse(team, new CountryResponse(countryMap.get(team.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team.getTypeId())))).collect(Collectors.toList());

        List<PlayerMiniResponse> playerResponses = players.stream().map(player -> new PlayerMiniResponse(player, new CountryResponse(countryMap.get(player.getCountryId())))).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(new Response(new SeriesResponse(existingSeries, new CountryResponse(country), new TourMiniResponse(tour), new SeriesTypeResponse(seriesType), new GameTypeResponse(gameType), teamResponses, playerResponses)));
    }

    @GetMapping("/cric/v1/series/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id)
    {
        Series series = seriesService.getById(id);
        if(null == series)
        {
            throw new NotFoundException("Series");
        }

        SeriesType seriesType = seriesTypeService.getById(series.getTypeId());
        GameType gameType = gameTypeService.getById(series.getGameTypeId());

        List<SeriesTeamsMap> seriesTeamsMaps = seriesTeamsMapService.getBySeriesIds(Collections.singletonList(id));
        List<Long> teamIds = seriesTeamsMaps.stream().map(SeriesTeamsMap::getTeamId).collect(Collectors.toList());
        List<Team> teams = teamService.getByIds(teamIds);
        List<Integer> teamTypeIds = new ArrayList<>();
        List<Long> countryIds = new ArrayList<>();

        for(Team team: teams)
        {
            teamTypeIds.add(team.getTypeId());
            countryIds.add(team.getCountryId());
        }

        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<Match> matches = matchService.getBySeriesId(id);

        List<Long> stadiumIds = new ArrayList<>();
        List<Integer> resultTypeIds = new ArrayList<>();
        List<Integer> winMarginTypeIds = new ArrayList<>();

        for(Match match: matches)
        {
            stadiumIds.add(match.getStadiumId());
            resultTypeIds.add(match.getResultTypeId());
            if(null != match.getWinMarginTypeId())
            {
                winMarginTypeIds.add(match.getWinMarginTypeId());
            }
        }
        List<Stadium> stadiums = stadiumService.getByIds(stadiumIds);
        Map<Long, Stadium> stadiumMap = new HashMap<>();
        for(Stadium stadium: stadiums)
        {
            stadiumMap.put(stadium.getId(), stadium);
            countryIds.add(stadium.getCountryId());
        }

        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        List<TeamResponse> teamResponses = teams.stream().map(team -> new TeamResponse(team, new CountryResponse(countryMap.get(team.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team.getTypeId())))).collect(Collectors.toList());
        Map<Long, TeamResponse> teamResponseMap = teamResponses.stream().collect(Collectors.toMap(TeamResponse::getId, teamResponse -> teamResponse));

        List<ResultType> resultTypes = resultTypeService.getByIds(resultTypeIds);
        Map<Integer, ResultType> resultTypeMap = resultTypes.stream().collect(Collectors.toMap(ResultType::getId, resultType -> resultType));
        List<WinMarginType> winMarginTypes = winMarginTypeService.getByIds(winMarginTypeIds);
        Map<Integer, WinMarginType> winMarginTypeMap = winMarginTypes.stream().collect(Collectors.toMap(WinMarginType::getId, winMarginType -> winMarginType));

        List<MatchMiniResponse> matchMiniResponses = matches.stream().map(match -> {
            Stadium stadium = stadiumMap.get(match.getStadiumId());
            return new MatchMiniResponse(
                    match,
                    teamResponseMap.get(match.getTeam1Id()),
                    teamResponseMap.get(match.getTeam2Id()),
                    resultTypeMap.get(match.getResultTypeId()),
                    winMarginTypeMap.getOrDefault(match.getWinMarginTypeId(), null),
                    new StadiumResponse(stadium, new CountryResponse(countryMap.get(stadium.getCountryId())))
            );
        }).collect(Collectors.toList());

        SeriesDetailedResponse seriesResponse = new SeriesDetailedResponse(series, seriesType, gameType, teamResponses, matchMiniResponses);

        return ResponseEntity.status(HttpStatus.OK).body(new Response(seriesResponse));
    }
}