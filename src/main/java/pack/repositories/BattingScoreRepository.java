package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.BattingScore;

import java.util.List;

public interface BattingScoreRepository extends JpaRepository<BattingScore, Long> {
    List<BattingScore> findAllByMatchId(Long matchId);
}
