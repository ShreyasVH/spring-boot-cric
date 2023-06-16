package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.models.GameType;
import com.springboot.cric.repositories.GameTypeRepository;

import java.util.List;

@Service
public class GameTypeService {
    @Autowired
    private GameTypeRepository gameTypeRepository;

    public GameType getById(Integer id) {
        return gameTypeRepository.findById(id).orElse(null);
    }

    public List<GameType> getByIds(List<Integer> ids) {
        return gameTypeRepository.findAllById(ids);
    }
}
