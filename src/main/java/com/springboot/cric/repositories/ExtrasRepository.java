package com.springboot.cric.repositories;

import com.springboot.cric.models.Extras;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExtrasRepository extends JpaRepository<Extras, Integer> {
    List<Extras> findAllByMatchId(Integer matchId);
}
