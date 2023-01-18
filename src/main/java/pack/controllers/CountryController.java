package pack.controllers;

import pack.models.db.Country;
import pack.models.requests.countries.CreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pack.services.CountryService;

import java.util.List;

@RequestMapping(value = "/cricbuzz")
@RestController
public class CountryController
{
    @Autowired
    private CountryService countryService;

    @PostMapping(value = "/countries")
    public Country create(@RequestBody CreateRequest createRequest) {
        return countryService.create(createRequest);
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public List<Country> getAll() {
        return countryService.getAll();
    }
}
