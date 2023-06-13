package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.Country;
import com.springboot.cric.responses.CountryResponse;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.services.PlayerService;
import com.springboot.cric.requests.players.CreateRequest;
import com.springboot.cric.responses.PlayerResponse;
import com.springboot.cric.responses.Response;
import com.springboot.cric.models.Player;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private CountryService countryService;

    @PostMapping("/cric/v1/players")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        Country country = countryService.getById(request.getCountryId());
        if(null == country) {
            throw new NotFoundException("Country");
        }

        Player player = playerService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new PlayerResponse(player, new CountryResponse(country))));
    }
}