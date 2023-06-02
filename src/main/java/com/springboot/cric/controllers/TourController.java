package com.springboot.cric.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.requests.tours.CreateRequest;
import com.springboot.cric.responses.TourResponse;
import com.springboot.cric.services.TourService;
import com.springboot.cric.responses.Response;

@RestController
public class TourController {
    @Autowired
    private TourService tourService;

    @PostMapping("/cric/v1/tours")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new TourResponse(this.tourService.create(request))));
    }
}