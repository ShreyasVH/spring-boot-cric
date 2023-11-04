package com.springboot.cric.repositories;

import com.springboot.cric.models.FielderDismissal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FielderDismissalRepository extends JpaRepository<FielderDismissal, Integer> {
    List<FielderDismissal> findAllByMatchPlayerIdIn(List<Integer> matchPlayerIds);
}
