package com.springboot.cric.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByNameAndCountryIdAndDateOfBirth(String name, Long countryId, LocalDate dateOfBirth);
    Page<Player> findByNameContainingIgnoreCase(String name, Pageable pageable);
    long countByNameContainingIgnoreCase(String name);
}