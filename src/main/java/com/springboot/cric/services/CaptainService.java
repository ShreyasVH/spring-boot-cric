package com.springboot.cric.services;

import com.springboot.cric.models.Captain;
import com.springboot.cric.repositories.CaptainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CaptainService {
    @Autowired
    private CaptainRepository captainRepository;

    public List<Captain> add(List<Long> playerIds, Map<Long, Integer> matchPlayerMap)
    {
        List<Captain> captains = playerIds.stream().map(playerId -> new Captain(null, matchPlayerMap.get(playerId))).collect(Collectors.toList());
        return captainRepository.saveAll(captains);
    }

    public List<Captain> getByMatchPlayerIds(List<Integer> matchPlayerIds)
    {
        return captainRepository.findAllByMatchPlayerIdIn(matchPlayerIds);
    }
}
