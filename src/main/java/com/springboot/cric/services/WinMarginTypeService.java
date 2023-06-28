package com.springboot.cric.services;

import com.springboot.cric.repositories.WinMarginTypeRepository;
import com.springboot.cric.models.WinMarginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinMarginTypeService {
    @Autowired
    private WinMarginTypeRepository winMarginTypeRepository;

    public WinMarginType getById(Integer id) {
        return winMarginTypeRepository.findById(id).orElse(null);
    }
}