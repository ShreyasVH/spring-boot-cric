package com.springboot.cric.services;

import com.springboot.cric.models.ManOfTheSeries;
import com.springboot.cric.repositories.ManOfTheSeriesRepository;
import com.springboot.cric.requests.players.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManOfTheSeriesService {
    @Autowired
    private ManOfTheSeriesRepository manOfTheSeriesRepository;

    public List<ManOfTheSeries> getBySeriesIds(List<Integer> seriesIds) {
        return manOfTheSeriesRepository.findAllBySeriesIdIn(seriesIds);
    }

    public void add(Integer seriesId, List<Long> playerIds) {
        List<ManOfTheSeries> manOfTheSeriesList = playerIds.stream().map(playerId -> new ManOfTheSeries(null, seriesId, playerId)).collect(Collectors.toList());
        manOfTheSeriesRepository.saveAll(manOfTheSeriesList);
    }

    public void delete(Integer seriesId, List<Long> playerIds) {
        List<ManOfTheSeries> manOfTheSeriesList = manOfTheSeriesRepository.findAllBySeriesIdAndPlayerIdIn(seriesId, playerIds);
        manOfTheSeriesRepository.deleteAll(manOfTheSeriesList);
    }

    public void remove(Integer seriesId)
    {
        manOfTheSeriesRepository.deleteAll(getBySeriesIds(Collections.singletonList(seriesId)));
    }

    public void merge(MergeRequest mergeRequest)
    {
        List<ManOfTheSeries> manOfTheSeriesList = manOfTheSeriesRepository.findAllByPlayerId(mergeRequest.getPlayerIdToMerge());
        manOfTheSeriesList.forEach(manOfTheSeries -> manOfTheSeries.setPlayerId(mergeRequest.getOriginalPlayerId()));
        manOfTheSeriesRepository.saveAll(manOfTheSeriesList);
    }
}
