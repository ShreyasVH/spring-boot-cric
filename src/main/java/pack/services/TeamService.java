package pack.services;

import pack.models.db.Team;
import pack.models.requests.teams.CreateRequest;

import java.util.List;

public interface TeamService {
    Team create(CreateRequest createRequest);
    List<Team> getAll();
}
