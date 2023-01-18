package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.SeriesType;

public interface SeriesTypeRepository extends JpaRepository<SeriesType, Integer> {
    SeriesType findByName(String name);
}
