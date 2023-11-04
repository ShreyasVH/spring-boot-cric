package com.springboot.cric.repositories;

import com.springboot.cric.models.WicketKeeper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WicketKeeperRepository extends JpaRepository<WicketKeeper, Integer> {
    List<WicketKeeper> findAllByMatchPlayerIdIn(List<Integer> matchPlayerIds);
}
