package com.springboot.cric.services;

import com.springboot.cric.models.BattingScore;
import com.springboot.cric.repositories.BattingScoreCustomRepository;
import com.springboot.cric.repositories.BattingScoreRepository;
import com.springboot.cric.requests.matches.BattingScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BattingScoreService {

    @Autowired
    private BattingScoreRepository battingScoreRepository;

    @Autowired
    private BattingScoreCustomRepository battingScoreCustomRepository;

    public List<BattingScore> add(List<BattingScoreRequest> battingScoreRequests, Map<Long, Integer> matchPlayerMaps)
    {
        List<BattingScore> battingScores = battingScoreRequests.stream().map(battingScoreRequest -> new BattingScore(battingScoreRequest, matchPlayerMaps)).collect(Collectors.toList());
        return battingScoreRepository.saveAll(battingScores);
    }

    public Map<String, Map<String, Integer>> getDismissalStats(Long playerId)
    {
        return battingScoreCustomRepository.getDismissalStats(playerId);
    }

    public Map<String, Map<String, Integer>> getBattingStats(Long playerId)
    {
        return battingScoreCustomRepository.getBattingStats(playerId);
    }

    public List<BattingScore> getBattingScores(List<Integer> matchPlayerIds)
    {
        return battingScoreRepository.findAllByMatchPlayerIdIn(matchPlayerIds);
    }

    public void remove(List<Integer> matchPlayerIds)
    {
        battingScoreRepository.deleteAll(getBattingScores(matchPlayerIds));
    }
}
