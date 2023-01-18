package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.Captain;

import java.util.List;

public interface CaptainRepository extends JpaRepository<Captain, Long> {
    List<Captain> findAllByMatchId(Long matchId);
}
