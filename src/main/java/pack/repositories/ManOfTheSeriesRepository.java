package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.models.db.ManOfTheSeries;

import java.util.List;

public interface ManOfTheSeriesRepository extends JpaRepository<ManOfTheSeries, Long> {
    List<ManOfTheSeries> findAllBySeriesId(Long seriesId);
}
