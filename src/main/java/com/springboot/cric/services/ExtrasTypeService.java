package com.springboot.cric.services;

import com.springboot.cric.repositories.ExtrasTypeRepository;
import com.springboot.cric.models.ExtrasType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtrasTypeService {
    @Autowired
    private ExtrasTypeRepository extrasTypeRepository;

    public List<ExtrasType> getAll() {
        return extrasTypeRepository.findAll();
    }
}