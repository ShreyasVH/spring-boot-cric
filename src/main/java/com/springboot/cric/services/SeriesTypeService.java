package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.cric.models.SeriesType;
import com.springboot.cric.repositories.SeriesTypeRepository;

import java.util.List;

@Service
public class SeriesTypeService {
    @Autowired
    private SeriesTypeRepository seriesTypeRepository;

    public SeriesType getById(Integer id) {
        return seriesTypeRepository.findById(id).orElse(null);
    }

    public List<SeriesType> getByIds(List<Integer> ids) {
        return seriesTypeRepository.findAllById(ids);
    }
}
