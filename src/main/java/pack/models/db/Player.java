package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.models.requests.players.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long countryId;
    private Long dateOfBirth;
    private String image;

    public Player(CreateRequest createRequest)
    {
        this.name = createRequest.getName();
        this.countryId = createRequest.getCountryId();
        this.dateOfBirth = createRequest.getDateOfBirth();
        this.image = createRequest.getImage();
    }
}
