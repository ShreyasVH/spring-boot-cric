package com.springboot.cric.services;

import com.springboot.cric.repositories.PlayerCustomRepository;
import com.springboot.cric.requests.FilterRequest;
import com.springboot.cric.responses.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
    @Autowired
    private PlayerCustomRepository playerCustomRepository;

    public StatsResponse getStats(FilterRequest filterRequest)
    {
        StatsResponse statsResponse = new StatsResponse();
        if("batting".equals(filterRequest.getType()))
        {
            statsResponse = playerCustomRepository.getBattingStats(filterRequest);
        }
        else if("bowling".equals(filterRequest.getType()))
        {
            statsResponse = playerCustomRepository.getBowlingStats(filterRequest);
        }
        else if("fielding".equals(filterRequest.getType()))
        {
            statsResponse = playerCustomRepository.getFieldingStats(filterRequest);
        }
        return statsResponse;
    }
}
