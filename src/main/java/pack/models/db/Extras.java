package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.enums.ExtrasType;

@Data
@Entity
@NoArgsConstructor
@Table(name = "extras")
public class Extras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long matchId;
    private ExtrasType type;
    private int runs;
    private Long battingTeam;
    private Long bowlingTeam;
    @Column(name = "innings_id")
    private int innings;
    @Column(name = "team_innings_id")
    private int teamInnings;
}
