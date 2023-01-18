package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.enums.TeamType;
import pack.models.requests.teams.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long countryId;
    @Column(name = "team_type_id")
    private TeamType teamType;

    public Team(CreateRequest createRequest)
    {
        this.name = createRequest.getName();
        this.countryId = createRequest.getCountryId();
        this.teamType = createRequest.getTeamType();
    }
}
