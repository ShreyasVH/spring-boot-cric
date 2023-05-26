package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.util.List;

import com.springboot.cric.requests.countries.CreateRequest;
import com.springboot.cric.responses.CountryResponse;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.responses.Response;
import com.springboot.cric.models.Country;

@RestController
public class CountryController {
	@Autowired
    private CountryService countryService;

    @PostMapping("/cric/v1/countries")
    public Response create(@RequestBody CreateRequest request)
    {
        return new Response(new CountryResponse(this.countryService.create(request)));
    }

    @GetMapping("/cric/v1/countries/name/{name}")
    public Response searchByName(@PathVariable String name) {
        List<Country> countries = this.countryService.searchByName(name);
        return new Response(countries.stream().map(country -> new CountryResponse(country)).collect(Collectors.toList()));
    }
}