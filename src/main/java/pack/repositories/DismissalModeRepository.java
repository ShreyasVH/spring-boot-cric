package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.DismissalMode;

public interface DismissalModeRepository extends JpaRepository<DismissalMode, Integer> {
}
