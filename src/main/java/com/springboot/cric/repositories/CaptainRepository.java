package com.springboot.cric.repositories;

import com.springboot.cric.models.Captain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptainRepository extends JpaRepository<Captain, Integer> {
}
