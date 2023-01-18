package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.enums.ResultType;
import pack.enums.WinMarginType;
import pack.models.requests.matches.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long series;
    @Column(name = "team_1")
    private Long team1;
    @Column(name = "team_2")
    private Long team2;
    private Long tossWinner;
    private Long batFirst;
    private ResultType result;
    private Long winner;
    private Integer winMargin;
    private WinMarginType winMarginType;
    private Long stadium;
    private Long startTime;
    private String tag;
    private boolean isOfficial;

    public Match(CreateRequest createRequest) {
        this.series = createRequest.getSeriesId();
        this.team1 = createRequest.getTeam1();
        this.team2 = createRequest.getTeam2();
        this.tossWinner = createRequest.getTossWinner();
        this.batFirst = createRequest.getBatFirst();
        this.winner = createRequest.getWinner();;
        this.winMargin = createRequest.getWinMargin();
        this.stadium = createRequest.getStadium();
        this.startTime = createRequest.getStartTime();
        this.tag = createRequest.getTag();
        this.isOfficial = createRequest.isOfficial();
        this.result = createRequest.getResult();
        this.winMarginType = createRequest.getWinMarginType();
    }
}
