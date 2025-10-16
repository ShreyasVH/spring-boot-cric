package com.springboot.cric.repositories;

import com.springboot.cric.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository  extends JpaRepository<Tag, Long> {
}
