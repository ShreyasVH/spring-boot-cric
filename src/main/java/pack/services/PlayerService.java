package pack.services;

import pack.models.db.Player;
import pack.models.requests.players.CreateRequest;

import java.util.List;

public interface PlayerService {
    Player create(CreateRequest createRequest);
    List<Player> getAll(int offset, int count);
}
