package com.springboot.cric.controllers;

import com.springboot.cric.requests.FilterRequest;
import com.springboot.cric.responses.Response;
import com.springboot.cric.responses.StatsResponse;
import com.springboot.cric.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
    @Autowired
    private StatsService statsService;

    @PostMapping("/cric/v1/stats")
    public ResponseEntity<Response> getStats(@RequestBody FilterRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(statsService.getStats(request)));
    }
}
