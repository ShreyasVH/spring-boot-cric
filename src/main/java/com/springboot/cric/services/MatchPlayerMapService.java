package com.springboot.cric.services;

import com.springboot.cric.models.ManOfTheSeries;
import com.springboot.cric.models.MatchPlayerMap;
import com.springboot.cric.repositories.MatchPlayerMapRepository;
import com.springboot.cric.requests.players.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchPlayerMapService {
    @Autowired
    private MatchPlayerMapRepository matchPlayerMapRepository;

    public List<MatchPlayerMap> add(Integer matchId, List<Long> playerIds, Map<Long, Long> playerTeamMap)
    {
        List<MatchPlayerMap> matchPlayerMaps = playerIds.stream().map(playerId -> new MatchPlayerMap(null, matchId, playerId, playerTeamMap.get(playerId))).collect(Collectors.toList());
        return matchPlayerMapRepository.saveAll(matchPlayerMaps);
    }

    public List<MatchPlayerMap> getByMatchId(Integer matchId)
    {
        return matchPlayerMapRepository.findAllByMatchId(matchId);
    }

    public void remove(Integer matchId)
    {
        matchPlayerMapRepository.deleteAll(getByMatchId(matchId));
    }

    public void merge(MergeRequest mergeRequest)
    {
        List<MatchPlayerMap> matchPlayerMaps = matchPlayerMapRepository.findAllByPlayerId(mergeRequest.getPlayerIdToMerge());
        matchPlayerMaps.forEach(matchPlayerMap -> matchPlayerMap.setPlayerId(mergeRequest.getOriginalPlayerId()));
        matchPlayerMapRepository.saveAll(matchPlayerMaps);
    }
}
