package pack.models.requests.series;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.enums.GameType;
import pack.enums.SeriesType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequest {
    private String name;
    private Long homeCountryId;
    private SeriesType type;
    private GameType gameType;
    private Long startTime;
    private Long tourId;
    private List<Long> teams = new ArrayList<>();
}
