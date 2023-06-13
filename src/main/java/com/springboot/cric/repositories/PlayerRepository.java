package com.springboot.cric.repositories;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.cric.models.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByNameAndCountryIdAndDateOfBirth(String name, Long countryId, LocalDate dateOfBirth);
}