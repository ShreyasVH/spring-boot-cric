package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.FielderDismissal;

import java.util.List;

public interface FielderDismissalRepository extends JpaRepository<FielderDismissal, Long> {
    List<FielderDismissal> findAllByScoreIdIn(List<Long> scoreId);
}
