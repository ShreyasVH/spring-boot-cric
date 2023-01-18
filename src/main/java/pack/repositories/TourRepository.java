package pack.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pack.models.db.Tour;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long>, PagingAndSortingRepository<Tour, Long> {
    List<Tour> findAllByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long startTime, Long endTIme, PageRequest pageRequest);

    @Query(value = "select extract(year from to_timestamp(start_time / 1000)) from tours", nativeQuery = true)
    List<Integer> findYears();
}
