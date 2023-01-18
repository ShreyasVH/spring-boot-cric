package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.TeamType;

public interface TeamTypeRepository extends JpaRepository<TeamType, Integer> {
    TeamType findByName(String name);
}
