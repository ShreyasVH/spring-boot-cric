package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.GameType;

public interface GameTypeRepository extends JpaRepository<GameType, Integer> {
    GameType findByName(String name);
}
