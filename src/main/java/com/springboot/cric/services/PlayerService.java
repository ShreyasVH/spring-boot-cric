package com.springboot.cric.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<Player> getAll(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.fromString("asc"), "name");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<Player> playersPage = playerRepository.findAll(pageRequest);
        return playersPage.getContent();
    }

    public long getTotalCount() {
        return playerRepository.count();
    }

    public List<Player> getByIds(List<Long> ids) {
        return playerRepository.findAllById(ids);
    }
}