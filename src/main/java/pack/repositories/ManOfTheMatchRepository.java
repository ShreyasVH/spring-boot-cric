package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.ManOfTheMatch;

import java.util.List;

public interface ManOfTheMatchRepository extends JpaRepository<ManOfTheMatch, Long> {
    List<ManOfTheMatch> findAllByMatchId(Long matchId);
}
