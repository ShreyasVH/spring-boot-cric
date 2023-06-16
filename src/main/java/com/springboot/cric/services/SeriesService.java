package com.springboot.cric.services;

import com.springboot.cric.exceptions.ConflictException;
import com.springboot.cric.models.Series;
import com.springboot.cric.repositories.SeriesRepository;
import com.springboot.cric.requests.series.CreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
