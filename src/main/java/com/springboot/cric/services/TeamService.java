package com.springboot.cric.services;

import com.springboot.cric.models.Stadium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Team;
import com.springboot.cric.repositories.TeamRepository;
import com.springboot.cric.requests.teams.CreateRequest;

import java.util.List;

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

    public List<Team> getAll(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.fromString("asc"), "name");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<Team> teamsPage = teamRepository.findAll(pageRequest);
        return teamsPage.getContent();
    }

    public long getTotalCount() {
        return teamRepository.count();
    }

    public List<Team> getByIds(List<Long> ids) {
        return teamRepository.findAllById(ids);
    }
}
