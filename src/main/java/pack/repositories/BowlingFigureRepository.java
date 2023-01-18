package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.BowlingFigure;

import java.util.List;

public interface BowlingFigureRepository extends JpaRepository<BowlingFigure, Long> {
    List<BowlingFigure> findAllByMatchId(Long matchId);
}
