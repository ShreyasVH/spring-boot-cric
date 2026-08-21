package com.springboot.cric.repositories;

import com.springboot.cric.models.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnershipRepository extends JpaRepository<Partnership, Integer> {
}
