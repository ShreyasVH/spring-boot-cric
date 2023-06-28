package com.springboot.cric.services;

import com.springboot.cric.models.DismissalMode;
import com.springboot.cric.repositories.DismissalModeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DismissalModeService {
    @Autowired
    private DismissalModeRepository dismissalModeRepository;

    public List<DismissalMode> getAll()
    {
        return dismissalModeRepository.findAll();
    }
}
