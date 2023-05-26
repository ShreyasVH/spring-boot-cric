package com.springboot.cric.services;

import com.springboot.cric.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.springboot.cric.models.Stadium;
import com.springboot.cric.repositories.StadiumRepository;
import com.springboot.cric.requests.stadiums.CreateRequest;
import com.springboot.cric.exceptions.ConflictException;

@Service
public class StadiumService {
    @Autowired
    private StadiumRepository stadiumRepository;

    public Stadium create(CreateRequest createRequest) {
        createRequest.validate();

        Stadium existingStadium = stadiumRepository.findByNameAndCountryId(createRequest.getName(), createRequest.getCountryId());
        if(null != existingStadium) {
            throw new ConflictException("Stadium");
        }

        Stadium stadium = new Stadium(createRequest);
        return stadiumRepository.save(stadium);
    }

    public List<Stadium> getAll(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.fromString("asc"), "name");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<Stadium> stadiumsPage = stadiumRepository.findAll(pageRequest);
        return stadiumsPage.getContent();
    }

    public long getTotalCount() {
        return stadiumRepository.count();
    }
}