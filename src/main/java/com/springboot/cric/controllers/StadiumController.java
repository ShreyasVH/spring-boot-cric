package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
}