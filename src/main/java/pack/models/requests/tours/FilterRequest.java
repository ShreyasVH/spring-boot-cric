package pack.models.requests.tours;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterRequest {
    private int year;
    private int offset = 0;
    private int count = 20;
}
