package pack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pack.models.db.Team;
import pack.models.requests.teams.CreateRequest;
import pack.services.TeamService;

import java.util.List;

@RestController
@RequestMapping(value = "/cricbuzz")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    public Team create(@RequestBody CreateRequest createRequest) {
        return teamService.create(createRequest);
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    public List<Team> getAll() {
        return teamService.getAll();
    }
}
