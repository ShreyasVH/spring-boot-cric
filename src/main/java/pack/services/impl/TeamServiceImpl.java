package pack.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.models.db.Team;
import pack.models.requests.teams.CreateRequest;
import pack.repositories.TeamRepository;
import pack.repositories.TeamTypeRepository;
import pack.services.TeamService;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamTypeRepository teamTypeRepository;

    @Override
    public Team create(CreateRequest createRequest) {
        Team team = new Team(createRequest);
//        team.setTeamTypeId(teamTypeRepository.findByName(createRequest.getTeamType()).getId());
        return teamRepository.save(team);
    }

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }
}
