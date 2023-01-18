package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.ExtrasType;

public interface ExtrasTypeRepository extends JpaRepository<ExtrasType, Integer> {
    ExtrasType findByName(String name);
}
