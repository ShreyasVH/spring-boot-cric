package com.springboot.cric.repositories;

import com.springboot.cric.models.ManOfTheMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManOfTheMatchRepository extends JpaRepository<ManOfTheMatch, Integer> {
}
