package com.springboot.cric.services;

import com.springboot.cric.models.FielderDismissal;
import com.springboot.cric.repositories.FielderDismissalCustomRepository;
import com.springboot.cric.repositories.FielderDismissalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FielderDismissalService {
    @Autowired
    private FielderDismissalRepository fielderDismissalRepository;
    @Autowired
    private FielderDismissalCustomRepository fielderDismissalCustomRepository;

    public List<FielderDismissal> add(Map<Integer, List<Long>> scorePlayerMap, Map<Long, Integer> matchPlayerMap)
    {
        List<FielderDismissal> fielderDismissals = new ArrayList<>();
        for(Map.Entry<Integer, List<Long>> scorePlayerEntry: scorePlayerMap.entrySet())
        {
            fielderDismissals.addAll(scorePlayerEntry.getValue().stream().map(playerId -> new FielderDismissal(null, scorePlayerEntry.getKey(), matchPlayerMap.get(playerId))).collect(Collectors.toList()));
        }
        return fielderDismissalRepository.saveAll(fielderDismissals);
    }

    public Map<String, Map<String, Integer>> getFieldingStats(Long playerId)
    {
        return fielderDismissalCustomRepository.getFieldingStats(playerId);
    }

    public List<FielderDismissal> get(List<Integer> matchPlayerIds)
    {
        return fielderDismissalRepository.findAllByMatchPlayerIdIn(matchPlayerIds);
    }
}
