package com.springboot.cric.services;

import com.springboot.cric.models.Total;
import com.springboot.cric.repositories.TotalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotalsService {
    @Autowired
    private TotalsRepository totalsRepository;

    public void add(List<Total> totals)
    {
        totalsRepository.saveAll(totals);
    }
}
