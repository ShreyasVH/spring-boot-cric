package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "batting_scores")
public class BattingScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long matchId;
    private Long playerId;
    private Long teamId;
    private int runs;
    private int balls;
    private int fours;
    private int sixes;
    @Column(name = "mode_of_dismissal_id")
    private Integer dismissalMode;
    @Column(name = "bowler_id")
    private Long bowlerDismissalId;
    @Column(name = "innings_id")
    private int innings;
    @Column(name = "team_innings_id")
    private int teamInnings;
}
