package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.SeriesType;

@Repository
public interface SeriesTypeRepository extends JpaRepository<SeriesType, Integer> {
}
