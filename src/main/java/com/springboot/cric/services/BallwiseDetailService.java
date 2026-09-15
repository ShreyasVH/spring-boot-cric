package com.springboot.cric.services;

import com.springboot.cric.models.BallwiseDetail;
import com.springboot.cric.repositories.BallwiseDetailRespository;
import com.springboot.cric.requests.matches.BallwiseDetailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BallwiseDetailService {

    @Autowired
    private BallwiseDetailRespository ballwiseDetailRepository;

    public List<BallwiseDetail> add(List<BallwiseDetailRequest> ballwiseDetailRequests, Map<Long, Integer> matchPlayerMaps)
    {
        List<BallwiseDetail> ballwiseDetailList = ballwiseDetailRequests.stream().map(ballwiseDetailRequest -> new BallwiseDetail(ballwiseDetailRequest, matchPlayerMaps)).collect(Collectors.toList());
        return ballwiseDetailRepository.saveAll(ballwiseDetailList);
    }

    public List<BallwiseDetail> getAll(List<Integer> matchPlayerIds)
    {
        return ballwiseDetailRepository.findAllByMatchPlayerIds(matchPlayerIds);
    }

    public void remove(List<Integer> matchPlayerIds)
    {
        ballwiseDetailRepository.deleteAll(getAll(matchPlayerIds));
    }
}
