package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.WicketKeeper;

import java.util.List;

public interface WicketKeeperRepository extends JpaRepository<WicketKeeper, Long> {
    List<WicketKeeper> findAllByMatchId(Long matchId);
}
