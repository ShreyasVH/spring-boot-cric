package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pack.models.db.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, PagingAndSortingRepository<Player, Long> {
}
