package com.springboot.cric.controllers;

import com.springboot.cric.responses.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.Country;
import com.springboot.cric.models.Player;
import com.springboot.cric.services.CountryService;
import com.springboot.cric.services.PlayerService;
import com.springboot.cric.requests.players.CreateRequest;

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

    @GetMapping("/cric/v1/players")
    public ResponseEntity<Response> getAll(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Player> players = playerService.getAll(page, limit);
        List<Long> countryIds = players.stream().map(Player::getCountryId).collect(Collectors.toList());
        List<Country> countries = countryService.getByIds(countryIds);
        Map<Long, Country> countryMap = countries.stream().collect(Collectors.toMap(Country::getId, country -> country));

        List<PlayerResponse> playerResponses = players.stream().map(player -> new PlayerResponse(player, new CountryResponse(countryMap.get(player.getCountryId())))).collect(Collectors.toList());
        long totalCount = 0L;
        if(page == 1) {
            totalCount = playerService.getTotalCount();
        }

        PaginatedResponse<PlayerResponse> paginatedResponse = new PaginatedResponse<>(totalCount, playerResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }
}