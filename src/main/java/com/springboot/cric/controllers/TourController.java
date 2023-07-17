package com.springboot.cric.controllers;

import com.springboot.cric.models.Country;
import com.springboot.cric.models.Player;
import com.springboot.cric.models.Tour;
import com.springboot.cric.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springboot.cric.requests.tours.CreateRequest;
import com.springboot.cric.services.TourService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TourController {
    @Autowired
    private TourService tourService;

    @PostMapping("/cric/v1/tours")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new TourResponse(this.tourService.create(request))));
    }

    @GetMapping("/cric/v1/tours/year/{year}")
    public ResponseEntity<Response> getAll(@PathVariable Integer year, @RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Tour> tours = tourService.getAllForYear(year, page, limit);

        List<TourResponse> tourResponses = tours.stream().map(TourResponse::new).collect(Collectors.toList());
        long totalCount = 0L;
        if(page == 1) {
            totalCount = tourService.getTotalCountForYear(year);
        }

        PaginatedResponse<TourResponse> paginatedResponse = new PaginatedResponse<>(totalCount, tourResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }

    @GetMapping("/cric/v1/tours/years")
    public ResponseEntity<Response> getAllYears() {
        List<Integer> years = tourService.getAllYears();

        return ResponseEntity.status(HttpStatus.OK).body(new Response(years));
    }
}