package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.Country;
import com.springboot.cric.responses.CountryResponse;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.services.StadiumService;
import com.springboot.cric.requests.stadiums.CreateRequest;
import com.springboot.cric.responses.StadiumResponse;
import com.springboot.cric.responses.Response;
import com.springboot.cric.responses.PaginatedResponse;
import com.springboot.cric.models.Stadium;

@RestController
public class StadiumController {
    @Autowired
    private StadiumService stadiumService;
    @Autowired
    private CountryService countryService;

    @PostMapping("/cric/v1/stadiums")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        Country country = countryService.getById(request.getCountryId());
        if(null == country) {
            throw new NotFoundException("Country");
        }

        Stadium stadium = stadiumService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new StadiumResponse(stadium, new CountryResponse(country))));
    }

    @GetMapping("/cric/v1/stadiums")
    public ResponseEntity<Response> getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Stadium> stadiums = stadiumService.getAll(page, limit);
        List<Long> countryIds = stadiums.stream().map(Stadium::getCountryId).collect(Collectors.toList());
        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        List<StadiumResponse> stadiumResponses = stadiums.stream().map(stadium -> new StadiumResponse(stadium, new CountryResponse(countryMap.get(stadium.getCountryId())))).collect(Collectors.toList());
        long totalCount = 0L;
        if(page == 1) {
            totalCount = stadiumService.getTotalCount();
        }

        PaginatedResponse<StadiumResponse> paginatedResponse = new PaginatedResponse<>(totalCount, stadiumResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }
}