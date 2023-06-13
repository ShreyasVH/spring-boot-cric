package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Player;
import com.springboot.cric.repositories.PlayerRepository;
import com.springboot.cric.requests.players.CreateRequest;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player create(CreateRequest createRequest) {
        createRequest.validate();

        Player existingPlayer = playerRepository.findByNameAndCountryIdAndDateOfBirth(createRequest.getName(), createRequest.getCountryId(), createRequest.getDateOfBirth());
        if(null != existingPlayer) {
            throw new ConflictException("Player");
        }

        Player player = new Player(createRequest);
        return playerRepository.save(player);
    }
}