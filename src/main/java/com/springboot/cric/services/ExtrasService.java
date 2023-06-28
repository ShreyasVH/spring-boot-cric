package com.springboot.cric.services;

import com.springboot.cric.models.Extras;
import com.springboot.cric.repositories.ExtrasRepository;
import com.springboot.cric.requests.matches.ExtrasRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtrasService {
    @Autowired
    private ExtrasRepository extrasRepository;


    public List<Extras> add(Integer matchId, List<ExtrasRequest> extrasRequests)
    {
        List<Extras> extrasList = extrasRequests.stream().map(extrasRequest -> new Extras(matchId, extrasRequest)).collect(Collectors.toList());
        return extrasRepository.saveAll(extrasList);
    }
}
