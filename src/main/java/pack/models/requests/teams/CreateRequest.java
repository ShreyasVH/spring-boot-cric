package pack.models.requests.teams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import pack.enums.TeamType;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRequest {
    private String name;
    private Long countryId;
    private TeamType teamType;
}
