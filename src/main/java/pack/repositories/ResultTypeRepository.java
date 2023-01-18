package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.ResultType;

public interface ResultTypeRepository extends JpaRepository<ResultType, Integer> {
    ResultType findByName(String name);
}
