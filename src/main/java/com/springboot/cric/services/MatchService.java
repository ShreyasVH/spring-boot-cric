package com.springboot.cric.services;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Match;
import com.springboot.cric.repositories.MatchRepository;
import com.springboot.cric.requests.matches.CreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public Match create(CreateRequest createRequest)
    {
        createRequest.validate();

        Match existingMatch = matchRepository.findByStadiumIdAndStartTime(createRequest.getStadiumId(), createRequest.getStartTime());
        if(null != existingMatch)
        {
            throw new ConflictException("Match");
        }

        Match match = new Match(createRequest);
        return matchRepository.save(match);
    }
}
