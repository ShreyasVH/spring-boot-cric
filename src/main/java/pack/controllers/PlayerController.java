package pack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.models.db.Player;
import pack.models.requests.players.CreateRequest;
import pack.services.PlayerService;

import java.util.List;

@RestController
@RequestMapping(value = "/cricbuzz")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public Player create(@RequestBody CreateRequest createRequest) {
        return playerService.create(createRequest);
    }

    @RequestMapping(value = "/players/all/{offset}/{count}", method = RequestMethod.GET)
    public List<Player> getAll(@PathVariable Integer offset, @PathVariable Integer count) {
        return playerService.getAll(offset, count);
    }
}
