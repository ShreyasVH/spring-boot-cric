package pack.models.requests.stadiums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRequest {
    private String name;
    private String city;
    private String state;
    private Long countryId;
}
