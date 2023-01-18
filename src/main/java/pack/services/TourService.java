package pack.services;

import pack.models.db.Tour;
import pack.models.requests.tours.CreateRequest;
import pack.models.requests.tours.FilterRequest;
import pack.models.responses.TourResponse;

import java.util.List;

public interface TourService {
    Tour create(CreateRequest createRequest);
    List<Tour> filter(FilterRequest filterRequest);
    TourResponse get(Long id);
    List<Integer> getYears();
}
