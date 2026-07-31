package com.springboot.cric.services;

import com.springboot.cric.models.TagMap;
import com.springboot.cric.repositories.TagMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagMapService {
    @Autowired
    private TagMapRepository tagMapRepository;

    public void create(Integer entityId, List<Integer> tagIds) {
        List<TagMap> tagMaps = tagIds.stream().map(tagId -> new TagMap(null, entityId, tagId)).collect(Collectors.toList());
        tagMapRepository.saveAll(tagMaps);
    }

    public void remove(Integer entityId, List<Integer> seriesTagIds)
    {
        tagMapRepository.deleteAll(get(entityId, seriesTagIds));
    }

    public List<TagMap> get(Integer entityId, List<Integer> tagIds) {
        return tagMapRepository.findAllByEntityIdAndTagIdIn(entityId, tagIds);
    }
}
