package com.springboot.cric.controllers;

import com.springboot.cric.models.*;
import com.springboot.cric.responses.*;

import java.util.ArrayList;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new SeriesResponse(series, new CountryResponse(country), new TourResponse(tour), new SeriesTypeResponse(seriesType), new GameTypeResponse(gameType), teamResponses)));
    }
}