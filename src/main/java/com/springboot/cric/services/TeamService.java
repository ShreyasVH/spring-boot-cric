package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Team;
import com.springboot.cric.repositories.TeamRepository;
import com.springboot.cric.requests.teams.CreateRequest;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team create(CreateRequest createRequest) {
        createRequest.validate();

        Team existingTeam = teamRepository.findByNameAndCountryIdAndTypeId(createRequest.getName(), createRequest.getCountryId(), createRequest.getTypeId());
        if(null != existingTeam) {
            throw new ConflictException("Team");
        }

        Team team = new Team(createRequest);
        return teamRepository.save(team);
    }
}
