package com.springboot.cric.services;

import com.springboot.cric.models.BowlingFigure;
import com.springboot.cric.models.Partnership;
import com.springboot.cric.repositories.PartnershipRepository;
import com.springboot.cric.requests.matches.PartnershipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PartnershipService {
    @Autowired
    private PartnershipRepository partnershipRepository;

    public List<Partnership> add(List<PartnershipRequest> partnershipRequests, Map<Long, Integer> matchPlayerMaps)
    {
        List<Partnership> partnerships = partnershipRequests.stream().flatMap(partnershipRequest -> Stream.of(
                new Partnership(partnershipRequest, matchPlayerMaps, true),
                new Partnership(partnershipRequest, matchPlayerMaps, false)
        )).collect(Collectors.toList());
        return partnershipRepository.saveAll(partnerships);
    }

    public List<Partnership> get(List<Integer> matchPlayerIds)
    {
        return partnershipRepository.findAllByMatchPlayerIds(matchPlayerIds);
    }

    public void remove(List<Integer> matchPlayerIds)
    {
        partnershipRepository.deleteAll(get(matchPlayerIds));
    }
}
