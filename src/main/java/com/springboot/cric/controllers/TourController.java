package com.springboot.cric.controllers;

import com.springboot.cric.exceptions.NotFoundException;
import com.springboot.cric.models.GameType;
import com.springboot.cric.models.Series;
import com.springboot.cric.models.Tour;
import com.springboot.cric.responses.*;
import com.springboot.cric.services.GameTypeService;
import com.springboot.cric.services.SeriesService;
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
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private GameTypeService gameTypeService;

    @PostMapping("/cric/v1/tours")
    public ResponseEntity<Response> create(@RequestBody CreateRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new TourMiniResponse(this.tourService.create(request))));
    }

    @GetMapping("/cric/v1/tours/year/{year}")
    public ResponseEntity<Response> getAll(@PathVariable Integer year, @RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        List<Tour> tours = tourService.getAllForYear(year, page, limit);

        List<TourMiniResponse> tourResponses = tours.stream().map(TourMiniResponse::new).collect(Collectors.toList());
        long totalCount = 0L;
        if(page == 1) {
            totalCount = tourService.getTotalCountForYear(year);
        }

        PaginatedResponse<TourMiniResponse> paginatedResponse = new PaginatedResponse<>(totalCount, tourResponses, page, limit);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(paginatedResponse));
    }

    @GetMapping("/cric/v1/tours/years")
    public ResponseEntity<Response> getAllYears() {
        List<Integer> years = tourService.getAllYears();

        return ResponseEntity.status(HttpStatus.OK).body(new Response(years));
    }

    @GetMapping("/cric/v1/tours/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        Tour tour = tourService.getById(id);
        if(null == tour)
        {
            throw new NotFoundException("Tour");
        }
        TourResponse tourResponse = new TourResponse(tour);
        List<Series> seriesList = seriesService.getByTourId(id);

        List<Integer> gameTypeIds = seriesList.stream().map(Series::getGameTypeId).collect(Collectors.toList());
        List<GameType> gameTypes = gameTypeService.getByIds(gameTypeIds);
        Map<Integer, GameType> gameTypeMap = gameTypes.stream().collect(Collectors.toMap(GameType::getId, gameType -> gameType));

        List<SeriesMiniResponse> seriesMiniResponses = seriesList.stream().map(series -> new SeriesMiniResponse(series, gameTypeMap.get(series.getGameTypeId()))).collect(Collectors.toList());
        tourResponse.setSeriesList(seriesMiniResponses);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(tourResponse));
    }
}