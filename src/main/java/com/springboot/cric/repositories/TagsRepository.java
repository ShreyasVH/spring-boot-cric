package com.springboot.cric.repositories;

import com.springboot.cric.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagsRepository  extends JpaRepository<Tag, Integer> {
    List<Tag> findByIdIn(List<Integer> ids);
}
