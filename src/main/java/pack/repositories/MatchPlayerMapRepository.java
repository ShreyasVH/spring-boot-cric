package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.MatchPlayerMap;

import java.util.List;

public interface MatchPlayerMapRepository extends JpaRepository<MatchPlayerMap, Long> {
    List<MatchPlayerMap> findAllByMatchId(Long matchId);
}
