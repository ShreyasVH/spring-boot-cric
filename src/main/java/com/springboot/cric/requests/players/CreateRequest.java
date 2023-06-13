package com.springboot.cric.requests.players;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import com.springboot.cric.exceptions.BadRequestException;


@Data
@NoArgsConstructor
public class CreateRequest {
    private String name;
    private Long countryId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    private String image;

    public void validate() {
        if (StringUtils.isEmpty(name)) {
            throw new BadRequestException("Invalid name");
        }

        if (null == dateOfBirth) {
            throw new BadRequestException("Invalid date of birth");
        }
    }
}