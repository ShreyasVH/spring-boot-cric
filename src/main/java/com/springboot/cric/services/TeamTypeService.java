package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.models.TeamType;
import com.springboot.cric.repositories.TeamTypeRepository;

import java.util.List;

@Service
public class TeamTypeService {
    @Autowired
    private TeamTypeRepository teamTypeRepository;

    public TeamType getById(Integer id) {
        return teamTypeRepository.findById(id).orElse(null);
    }

    public List<TeamType> getByIds(List<Integer> ids) {
        return teamTypeRepository.findAllById(ids);
    }
}
