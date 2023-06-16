package com.springboot.cric.services;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Player;
import com.springboot.cric.models.Series;
import com.springboot.cric.repositories.SeriesRepository;
import com.springboot.cric.requests.series.CreateRequest;
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
}
