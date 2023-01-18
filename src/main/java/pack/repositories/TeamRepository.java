package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pack.models.db.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
