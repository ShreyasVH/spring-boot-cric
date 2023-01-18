package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pack.models.db.Stadium;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
}
