package com.springboot.cric.services;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Player;
import com.springboot.cric.models.Series;
import com.springboot.cric.repositories.SeriesRepository;
import com.springboot.cric.requests.series.CreateRequest;
import com.springboot.cric.requests.series.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    public Series create(CreateRequest createRequest) {
        createRequest.validate();

        Series existingSeries = seriesRepository.findByNameAndTourIdAndGameTypeId(createRequest.getName(), createRequest.getTourId(), createRequest.getGameTypeId());
        if(null != existingSeries) {
            throw new ConflictException("Series");
        }

        Series series = new Series(createRequest);
        return seriesRepository.save(series);
    }

    public List<Series> getAll(int page, int limit) {
        Sort sort = Sort.by(Sort.Direction.fromString("asc"), "name");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<Series> seriesPage = seriesRepository.findAll(pageRequest);
        return seriesPage.getContent();
    }

    public long getTotalCount() {
        return seriesRepository.count();
    }

    public Series getById(Long id) {
        return seriesRepository.getOne(id);
    }

    public Series update(Series existingSeries, UpdateRequest updateRequest) {
        boolean isUpdateRequired = false;
        if (null != updateRequest.getName() && !updateRequest.getName().equals(existingSeries.getName())) {
            isUpdateRequired = true;
            existingSeries.setName(updateRequest.getName());
        }

        if (null != updateRequest.getHomeCountryId() && !updateRequest.getHomeCountryId().equals(existingSeries.getHomeCountryId())) {
            isUpdateRequired = true;
            existingSeries.setHomeCountryId(updateRequest.getHomeCountryId());
        }

        if (null != updateRequest.getTourId() && !updateRequest.getTourId().equals(existingSeries.getTourId())) {
            isUpdateRequired = true;
            existingSeries.setTourId(updateRequest.getTourId());
        }

        if (null != updateRequest.getTypeId() && !updateRequest.getTypeId().equals(existingSeries.getTypeId())) {
            isUpdateRequired = true;
            existingSeries.setTypeId(updateRequest.getTypeId());
        }

        if (null != updateRequest.getGameTypeId() && !updateRequest.getGameTypeId().equals(existingSeries.getGameTypeId())) {
            isUpdateRequired = true;
            existingSeries.setGameTypeId(updateRequest.getGameTypeId());
        }

        if (null != updateRequest.getStartTime() && !updateRequest.getStartTime().equals(existingSeries.getStartTime())) {
            isUpdateRequired = true;
            existingSeries.setStartTime(updateRequest.getStartTime());
        }

        if (isUpdateRequired) {
            seriesRepository.save(existingSeries);
        }

        return existingSeries;
    }

    public List<Series> getByTourId(Long tourId)
    {
        return seriesRepository.findAllByTourIdOrderByStartTimeDesc(tourId);
    }

    public void remove(Long id)
    {
        seriesRepository.deleteById(id);
    }
}
