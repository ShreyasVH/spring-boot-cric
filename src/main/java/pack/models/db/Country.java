package pack.models.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.models.requests.countries.CreateRequest;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "countries")
public class Country
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Country(CreateRequest request)
    {
        this.name = request.getName();
    }
}
