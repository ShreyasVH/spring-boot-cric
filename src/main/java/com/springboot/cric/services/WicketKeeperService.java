package com.springboot.cric.services;

import com.springboot.cric.models.WicketKeeper;
import com.springboot.cric.repositories.WicketKeeperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WicketKeeperService {
    @Autowired
    private WicketKeeperRepository wicketKeeperRepository;

    public List<WicketKeeper> add(List<Long> playerIds, Map<Long, Integer> matchPlayerMap)
    {
        List<WicketKeeper> wicketKeepers = playerIds.stream().map(playerId -> new WicketKeeper(null, matchPlayerMap.get(playerId))).collect(Collectors.toList());
        return wicketKeeperRepository.saveAll(wicketKeepers);
    }

    public List<WicketKeeper> getByMatchPlayerIds(List<Integer> matchPlayerIds)
    {
        return wicketKeeperRepository.findAllByMatchPlayerIdIn(matchPlayerIds);
    }

    public void remove(List<Integer> matchPlayerIds)
    {
        wicketKeeperRepository.deleteAll(getByMatchPlayerIds(matchPlayerIds));
    }
}
