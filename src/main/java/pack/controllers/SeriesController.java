package pack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.models.db.Series;
import pack.models.requests.series.CreateRequest;
import pack.models.responses.SeriesResponse;
import pack.services.SeriesService;

import java.util.List;

@RestController
@RequestMapping(value = "/cricbuzz")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @RequestMapping(value = "/series", method = RequestMethod.POST)
    public Series create(@RequestBody CreateRequest createRequest) {
        return seriesService.create(createRequest);
    }

    @RequestMapping(value = "/series", method = RequestMethod.GET)
    public List<Series> getAll() {
        return seriesService.getAll();
    }

    @RequestMapping(value = "/series/{id}", method = RequestMethod.GET)
    public SeriesResponse get(@PathVariable Long id) {
        return seriesService.get(id);
    }
}
