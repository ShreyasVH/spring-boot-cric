package pack.models.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.models.requests.stadiums.CreateRequest;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "stadiums")
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String state;
    private Long countryId;

    public Stadium(CreateRequest createRequest)
    {
        this.name = createRequest.getName();
        this.city = createRequest.getCity();
        this.state = createRequest.getState();
        this.countryId = createRequest.getCountryId();
    }
}
