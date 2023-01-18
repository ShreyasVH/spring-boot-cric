package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.enums.GameType;
import pack.enums.SeriesType;
import pack.models.requests.series.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long homeCountryId;
    private Long tourId;
    private SeriesType type;
    private GameType gameType;
    private Long startTime;

    public Series(CreateRequest createRequest) {
        this.name = createRequest.getName();
        this.homeCountryId = createRequest.getHomeCountryId();
        this.tourId = createRequest.getTourId();
        this.startTime = createRequest.getStartTime();
        this.type = createRequest.getType();
        this.gameType = createRequest.getGameType();
    }
}
