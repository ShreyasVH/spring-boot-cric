package com.springboot.cric.repositories;

import com.springboot.cric.models.TagMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagMapRepository extends JpaRepository<TagMap, Long> {
    List<TagMap> findAllByEntityTypeAndEntityId(String entityType, Integer entityId);
}
