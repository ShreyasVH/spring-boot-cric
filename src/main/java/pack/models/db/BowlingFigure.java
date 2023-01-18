package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "bowling_figures")
public class BowlingFigure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long matchId;
    private Long playerId;
    private Long teamId;
    private int balls;
    private int maidens;
    private int runs;
    private int wickets;
    @Column(name = "innings_id")
    private int innings;
    @Column(name = "team_innings_id")
    private int teamInnings;
}
