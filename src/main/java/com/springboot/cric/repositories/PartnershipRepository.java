package com.springboot.cric.repositories;

import com.springboot.cric.models.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, Integer> {
    @Query("""
    SELECT p FROM Partnership p WHERE p.matchPlayerId1 IN :matchPlayerIds OR p.matchPlayerId2 IN :matchPlayerIds
    """)
    List<Partnership> findAllByMatchPlayerIds(@Param("matchPlayerIds") List<Integer> matchPlayerIds);
}
