package pack.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pack.models.db.Player;
import pack.models.requests.players.CreateRequest;
import pack.repositories.PlayerRepository;
import pack.services.PlayerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player create(CreateRequest createRequest) {
        Player player = new Player(createRequest);
        return playerRepository.save(player);
    }

    @Override
    public List<Player> getAll(int offset, int count) {
        return playerRepository.findAll(PageRequest.of(offset / count, count)).getContent();
    }
}
