package com.springboot.cric.services;

import com.springboot.cric.repositories.TourCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.cric.models.Tour;
import com.springboot.cric.repositories.TourRepository;
import com.springboot.cric.requests.tours.CreateRequest;
import com.springboot.cric.exceptions.ConflictException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourCustomRepository tourCustomRepository;

    public Tour create(CreateRequest createRequest) {
        createRequest.validate();

        Tour existingTour = tourRepository.findByNameAndStartTime(createRequest.getName(), createRequest.getStartTime());
        if(null != existingTour) {
            throw new ConflictException("Tour");
        }

        Tour tour = new Tour(createRequest);
        return tourRepository.save(tour);
    }

    public Tour getById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    public List<Tour> getByIds(List<Long> ids) {
        return (List<Tour>) tourRepository.findAllById(ids);
    }

    public List<Tour> getAllForYear(int year, int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.fromString("desc"), "startTime");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        LocalDateTime startTime = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endTime = startTime.plusYears(1L);
        Page<Tour> toursPage = tourRepository.findAllByStartTimeBetween(startTime, endTime, pageRequest);
        return toursPage.getContent();
    }

    public long getTotalCountForYear(int year) {
        LocalDateTime startTime = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endTime = startTime.plusYears(1L);
        return tourRepository.countAllByStartTimeBetween(startTime, endTime);
    }

    public List<Integer> getAllYears() {
        return tourCustomRepository.getAllYears();
    }
}