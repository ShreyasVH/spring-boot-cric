package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.BowlerDismissal;

public interface BowlerDismissalRepository extends JpaRepository<BowlerDismissal, Long> {
}
