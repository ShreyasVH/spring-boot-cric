package pack.models.requests.matches;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.enums.ResultType;
import pack.enums.WinMarginType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequest {
    private Long seriesId;
    private Long team1;
    private Long team2;
    private Long tossWinner;
    private Long batFirst;
    private ResultType result;
    private Long winner;
    private int winMargin;
    private WinMarginType winMarginType;
    private Long stadium;
    private Long startTime;
    private String tag;
    private List<Map<String, String>> players;
    private List<Map<String, String>> bench;
    private List<Map<String, String>> extras = new ArrayList<>();
    private List<Map<String, String>> battingScores = new ArrayList<>();
    private List<Map<String, String>> bowlingFigures = new ArrayList<>();
    private List<Long> manOfTheMatchList = new ArrayList<>();
    private List<Long> captains = new ArrayList<>();
    private List<Long> wicketKeepers = new ArrayList<>();
    private boolean isOfficial = true;
}
