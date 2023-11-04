package com.springboot.cric.repositories;

import com.springboot.cric.models.Captain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaptainRepository extends JpaRepository<Captain, Integer> {
    List<Captain> findAllByMatchPlayerIdIn(List<Integer> matchPlayerIds);
}
