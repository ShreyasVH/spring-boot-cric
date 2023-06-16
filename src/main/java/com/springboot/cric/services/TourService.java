package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.models.Tour;
import com.springboot.cric.repositories.TourRepository;
import com.springboot.cric.requests.tours.CreateRequest;
import com.springboot.cric.exceptions.ConflictException;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    public Tour create(CreateRequest createRequest) {
        createRequest.validate();

        Tour existingTour = tourRepository.findByNameAndStartTime(createRequest.getName(), createRequest.getStartTime());
        if(null != existingTour) {
            throw new ConflictException("Country");
        }

        Tour tour = new Tour(createRequest);
        return tourRepository.save(tour);
    }

    public Tour getById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }
}