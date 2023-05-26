package com.springboot.cric.requests.stadiums;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import com.springboot.cric.exceptions.BadRequestException;

@Data
@NoArgsConstructor
public class CreateRequest {
    private String name;
    private String city;
    private String state;
    private Long countryId;

    public void validate() {
        if (StringUtils.isEmpty(name)) {
            throw new BadRequestException("Invalid name");
        }

        if (StringUtils.isEmpty(city)) {
            throw new BadRequestException("Invalid city");
        }
    }
}