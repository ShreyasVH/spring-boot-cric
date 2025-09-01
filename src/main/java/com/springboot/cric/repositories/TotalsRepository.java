package com.springboot.cric.repositories;

import com.springboot.cric.models.Total;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalsRepository extends JpaRepository<Total, Integer> {
}
