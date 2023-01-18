package pack.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.models.db.Series;
import pack.models.db.Tour;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse {
    private Long id;
    private String name;
    private Long startTime;
    private List<Series> seriesList = new ArrayList<>();

    public TourResponse(Tour tour)
    {
        this.id = tour.getId();
        this.name = tour.getName();
        this.startTime = tour.getStartTime();
    }
}
