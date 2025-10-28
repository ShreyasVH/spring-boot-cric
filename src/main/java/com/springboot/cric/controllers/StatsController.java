package com.springboot.cric.controllers;

import com.springboot.cric.requests.FilterRequest;
import com.springboot.cric.responses.Response;
import com.springboot.cric.responses.StatsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {
    @PostMapping("/cric/v1/stats")
    public ResponseEntity<Response> create(@RequestBody FilterRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(new StatsResponse()));
    }
}
