package pack.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pack.models.db.Tour;
import pack.models.requests.tours.CreateRequest;
import pack.models.requests.tours.FilterRequest;
import pack.repositories.SeriesRepository;
import pack.repositories.TourRepository;
import pack.services.TourService;
import pack.models.responses.TourResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TourServiceImpl implements TourService {
    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    public TourResponse tourResponse(Tour tour)
    {
        TourResponse tourResponse = new TourResponse(tour);
        tourResponse.setSeriesList(seriesRepository.findAllByTourId(tour.getId()));

        return tourResponse;
    }

    @Override
    public Tour create(CreateRequest createRequest) {
        Tour tour = new Tour(createRequest);
        return tourRepository.save(tour);
    }

    @Override
    public List<Tour> filter(FilterRequest filterRequest) {
        Integer year = filterRequest.getYear();
        Integer offset = filterRequest.getOffset();
        Integer count = filterRequest.getCount();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startTime = calendar.getTime();

        calendar.set(Calendar.YEAR, year + 1);
        Date endTime = calendar.getTime();

        return tourRepository.findAllByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(startTime.getTime(), endTime.getTime(), PageRequest.of(offset / count, count));
    }

    @Override
    public TourResponse get(Long id) {
        return tourResponse(tourRepository.findById(id).orElse(null));
    }

    @Override
    public List<Integer> getYears() {
        return tourRepository.findYears();
    }
}
