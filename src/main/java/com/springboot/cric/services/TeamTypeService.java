package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.models.TeamType;
import com.springboot.cric.repositories.TeamTypeRepository;

@Service
public class TeamTypeService {
    @Autowired
    private TeamTypeRepository teamTypeRepository;

    public TeamType getById(Integer id) {
        return teamTypeRepository.findById(id).orElse(null);
    }
}
