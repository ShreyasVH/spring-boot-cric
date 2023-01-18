package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.WinMarginType;

public interface WinMarginTypeRepository extends JpaRepository<WinMarginType, Integer> {
    WinMarginType findByName(String name);
}
