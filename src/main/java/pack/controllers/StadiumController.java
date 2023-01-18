package pack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.models.db.Stadium;
import pack.models.requests.stadiums.CreateRequest;
import pack.services.StadiumService;

import java.util.List;

@RequestMapping(value = "/cricbuzz")
@RestController
public class StadiumController {
    @Autowired
    private StadiumService stadiumService;

    @RequestMapping(value = "/stadiums", method = RequestMethod.POST)
    public Stadium create (@RequestBody CreateRequest createRequest) {
        return stadiumService.create(createRequest);
    }

    @RequestMapping(value = "/stadiums", method = RequestMethod.GET)
    public List<Stadium> getAll () {
        return stadiumService.getAll();
    }
}
