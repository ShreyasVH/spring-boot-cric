package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.Extras;

import java.util.List;

public interface ExtrasRepository extends JpaRepository<Extras, Long> {
    List<Extras> findAllByMatchId(Long matchId);
}
