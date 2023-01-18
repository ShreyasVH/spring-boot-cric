package pack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.models.db.Match;
import pack.models.requests.matches.CreateRequest;
import pack.models.responses.MatchResponse;
import pack.services.MatchService;

@RestController
@RequestMapping(value = "/cricbuzz")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @RequestMapping(value = "/matches", method = RequestMethod.POST)
    public Match create(@RequestBody CreateRequest createRequest) {
        return matchService.create(createRequest);
    }

    @RequestMapping(value = "/matches/{id}", method = RequestMethod.GET)
    public MatchResponse get(@PathVariable Long id) {
        return matchService.get(id);
    }
}
