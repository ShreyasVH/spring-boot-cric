package pack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.models.db.Tour;
import pack.models.requests.tours.CreateRequest;
import pack.models.requests.tours.FilterRequest;
import pack.models.responses.TourResponse;
import pack.services.TourService;

import java.util.List;

@RestController
@RequestMapping(value = "/cricbuzz")
public class TourController {
    @Autowired
    private TourService tourService;

    @RequestMapping(value = "/tours", method = RequestMethod.POST)
    public Tour create(@RequestBody CreateRequest createRequest) {
        return tourService.create(createRequest);
    }

    @RequestMapping(value = "/tours/filter", method = RequestMethod.POST)
    public List<Tour> filter(@RequestBody FilterRequest filterRequest) {
        return tourService.filter(filterRequest);
    }

    @RequestMapping(value = "/tours/{id}", method = RequestMethod.GET)
    public TourResponse get(@PathVariable Long id) {
        return tourService.get(id);
    }

    @RequestMapping(value = "/tours/years", method = RequestMethod.GET)
    public List<Integer> getYears() {
        return tourService.getYears();
    }
}
