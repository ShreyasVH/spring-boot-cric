package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.Match;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllBySeries(Long series);
}
