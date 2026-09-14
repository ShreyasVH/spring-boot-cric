package com.springboot.cric.repositories;

import com.springboot.cric.models.BallwiseDetail;
import com.springboot.cric.models.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BallwiseDetailRespository extends JpaRepository<BallwiseDetail, Integer> {
    @Query("""
    SELECT bd FROM BallwiseDetail bd WHERE bd.batsmanMatchPlayerId IN :matchPlayerIds OR bd.bowlerMatchPlayerId IN :matchPlayerIds
    """)
    List<BallwiseDetail> findAllByMatchPlayerIds(@Param("matchPlayerIds") List<Integer> matchPlayerIds);
}
