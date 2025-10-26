package com.springboot.cric.services;

import com.springboot.cric.models.Tag;
import com.springboot.cric.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {
    @Autowired
    private TagsRepository tagsRepository;

    public List<Tag> getAll(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.fromString("asc"), "name");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<Tag> tagsPage = tagsRepository.findAll(pageRequest);
        return tagsPage.getContent();
    }

    public long getTotalCount() {
        return tagsRepository.count();
    }

    public List<Tag> getByIds(List<Integer> ids) {
        return tagsRepository.findByIdIn(ids);
    }
}
