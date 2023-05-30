package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.Country;
import com.springboot.cric.models.Team;
import com.springboot.cric.models.TeamType;
import com.springboot.cric.requests.teams.CreateRequest;
import com.springboot.cric.responses.CountryResponse;
import com.springboot.cric.responses.Response;
import com.springboot.cric.responses.TeamResponse;
import com.springboot.cric.responses.TeamTypeResponse;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.services.TeamService;
import com.springboot.cric.services.TeamTypeService;

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
}
