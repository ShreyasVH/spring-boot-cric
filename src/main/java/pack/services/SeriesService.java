package pack.services;

import pack.models.db.Series;
import pack.models.requests.series.CreateRequest;
import pack.models.responses.SeriesResponse;

import java.util.List;

public interface SeriesService {
    Series create(CreateRequest createRequest);
    List<Series> getAll();
    SeriesResponse get(Long id);
}
