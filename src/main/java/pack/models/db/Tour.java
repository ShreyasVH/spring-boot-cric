package pack.models.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pack.models.requests.tours.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long startTime;

    public Tour(CreateRequest createRequest) {
        this.name = createRequest.getName();
        this.startTime = createRequest.getStartTime();
    }
}
