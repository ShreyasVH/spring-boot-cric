package com.springboot.cric.services;

import com.springboot.cric.models.BowlingFigure;
import com.springboot.cric.repositories.BowlingFigureRepository;
import com.springboot.cric.requests.matches.BowlingFigureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BowlingFigureService {
    @Autowired
    private BowlingFigureRepository bowlingFigureRepository;

    public List<BowlingFigure> add(List<BowlingFigureRequest> bowlingFigureRequests, Map<Long, Integer> matchPlayerMap)
    {
        List<BowlingFigure> bowlingFigures = bowlingFigureRequests.stream().map(bowlingFigureRequest -> new BowlingFigure(bowlingFigureRequest, matchPlayerMap)).collect(Collectors.toList());
        return bowlingFigureRepository.saveAll(bowlingFigures);
    }
}
