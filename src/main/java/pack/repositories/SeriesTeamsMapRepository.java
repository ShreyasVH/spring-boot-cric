package pack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pack.models.db.SeriesTeamsMap;

import java.util.List;

@Repository
public interface SeriesTeamsMapRepository extends JpaRepository<SeriesTeamsMap, Long> {
    List<SeriesTeamsMap> findAllBySeriesId(Long seriesId);
}
