package com.springboot.cric.repositories;

import com.springboot.cric.models.WicketKeeper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WicketKeeperRepository extends JpaRepository<WicketKeeper, Integer> {
}
