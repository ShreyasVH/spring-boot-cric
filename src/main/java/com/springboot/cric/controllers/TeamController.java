package com.springboot.cric.controllers;

import com.springboot.cric.models.Stadium;
import com.springboot.cric.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.Country;
import com.springboot.cric.models.Team;
import com.springboot.cric.models.TeamType;
import com.springboot.cric.requests.teams.CreateRequest;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.services.TeamService;
import com.springboot.cric.services.TeamTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamTypeService teamTypeService;
    @Autowired
    private CountryService countryService;

    @PostMapping("/cric/v1/teams")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        Country country = countryService.getById(request.getCountryId());
        if(null == country) {
            throw new NotFoundException("Country");
        }

        TeamType teamType = teamTypeService.getById(request.getTypeId());
        if(null == teamType) {
            throw new NotFoundException("Team type");
        }

        Team team = teamService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new TeamResponse(team, new CountryResponse(country), new TeamTypeResponse(teamType))));
    }

    @GetMapping("/cric/v1/teams")
    public ResponseEntity<Response> getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Team> teams = teamService.getAll(page, limit);

        List<Long> countryIds = new ArrayList<>();
        List<Integer> teamTypeIds = new ArrayList<>();
        for(Team team: teams) {
            countryIds.add(team.getCountryId());
            teamTypeIds.add(team.getTypeId());
        }

        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        List<TeamType> teamTypes = teamTypeService.getByIds(teamTypeIds);
        Map<Integer, TeamType> teamTypeMap = teamTypes.stream().collect(Collectors.toMap(TeamType::getId, teamType -> teamType));

        List<TeamResponse> teamResponses = teams.stream().map(team -> new TeamResponse(team, new CountryResponse(countryMap.get(team.getCountryId())), new TeamTypeResponse(teamTypeMap.get(team.getTypeId())))).collect(Collectors.toList());
        long totalCount = 0L;
        if(page == 1) {
            totalCount = teamService.getTotalCount();
        }

        PaginatedResponse<TeamResponse> paginatedResponse = new PaginatedResponse<>(totalCount, teamResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }
}
