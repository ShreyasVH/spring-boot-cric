package com.springboot.cric.requests.teams;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import com.springboot.cric.exceptions.BadRequestException;

@Data
@NoArgsConstructor
public class CreateRequest {
    private String name;
    private Long countryId;
    private Integer typeId;

    public void validate() {
        if (StringUtils.isEmpty(name)) {
            throw new BadRequestException("Invalid name");
        }
    }
}
