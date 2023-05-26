package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.requests.countries.CreateRequest;
import com.springboot.cric.responses.CountryResponse;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.responses.Response;
import com.springboot.cric.responses.PaginatedResponse;
import com.springboot.cric.models.Country;

@RestController
public class CountryController {
	@Autowired
    private CountryService countryService;

    @PostMapping("/cric/v1/countries")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new CountryResponse(this.countryService.create(request))));
    }

    @GetMapping("/cric/v1/countries/name/{name}")
    public Response searchByName(@PathVariable String name) {
        List<Country> countries = this.countryService.searchByName(name);
        return new Response(countries.stream().map(country -> new CountryResponse(country)).collect(Collectors.toList()));
    }

    @GetMapping("/cric/v1/countries")
    public Response getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Country> countries = countryService.getAll(page, limit);
        List<CountryResponse> countryResponses = countries.stream().map(country -> new CountryResponse(country)).collect(Collectors.toList());
        long totalCount = 0L;
        if(page == 1) {
            totalCount = countryService.getTotalCount();
        }

        PaginatedResponse<CountryResponse> paginatedResponse = new PaginatedResponse(totalCount, countryResponses, page, limit);
        return new Response(paginatedResponse);
    }
}