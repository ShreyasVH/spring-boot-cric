package com.springboot.cric.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.Player;

@Data
@NoArgsConstructor
public class PlayerResponse {
    private Long id;
    private String name;
    private CountryResponse country;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    private String image;

    public PlayerResponse(Player player, CountryResponse country) {
        this.id = player.getId();
        this.name = player.getName();
        this.country = country;
        this.dateOfBirth = player.getDateOfBirth();
        this.image = player.getImage();
    }
}