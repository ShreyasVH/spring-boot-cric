package com.springboot.cric.repositories;

import com.springboot.cric.models.ManOfTheMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManOfTheMatchRepository extends JpaRepository<ManOfTheMatch, Integer> {
    List<ManOfTheMatch> findAllByMatchPlayerIdIn(List<Integer> matchPlayerIds);
}
