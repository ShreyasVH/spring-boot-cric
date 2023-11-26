package com.springboot.cric.services;

import com.springboot.cric.models.ManOfTheMatch;
import com.springboot.cric.repositories.ManOfTheMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ManOfTheMatchService {
    @Autowired
    private ManOfTheMatchRepository manOfTheMatchRepository;

    public List<ManOfTheMatch> add(List<Long> playerIds, Map<Long, Integer> matchPlayerMap)
    {
        List<ManOfTheMatch> manOfTheMatchList = playerIds.stream().map(playerId -> new ManOfTheMatch(null, matchPlayerMap.get(playerId))).collect(Collectors.toList());
        return manOfTheMatchRepository.saveAll(manOfTheMatchList);
    }

    public List<ManOfTheMatch> getByMatchPlayerIds(List<Integer> matchPlayerIds)
    {
        return manOfTheMatchRepository.findAllByMatchPlayerIdIn(matchPlayerIds);
    }

    public void remove(List<Integer> matchPlayerIds)
    {
        manOfTheMatchRepository.deleteAll(getByMatchPlayerIds(matchPlayerIds));
    }
}
