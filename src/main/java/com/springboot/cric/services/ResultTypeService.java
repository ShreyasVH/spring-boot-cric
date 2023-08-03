package com.springboot.cric.services;

import com.springboot.cric.repositories.ResultTypeRepository;
import com.springboot.cric.models.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultTypeService {
    @Autowired
    private ResultTypeRepository resultTypeRepository;

    public ResultType getById(Integer id) {
        return resultTypeRepository.findById(id).orElse(null);
    }

    public List<ResultType> getByIds(List<Integer> ids) {
        return resultTypeRepository.findAllById(ids);
    }
}