package com.springboot.cric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.GameType;

@Repository
public interface GameTypeRepository extends JpaRepository<GameType, Integer> {
}
