package com.springboot.cric.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.springboot.cric.models.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponse {
    private Long id;
    private String name;
    private CountryResponse country;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    private String image;
    private Map<String, Map<String, Integer>> dismissalStats = new HashMap<>();
    private Map<String, BattingStats> battingStats = new HashMap<>();
    private Map<String, FieldingStats> fieldingStats = new HashMap<>();
    private Map<String, BowlingStats> bowlingStats = new HashMap<>();

    public PlayerResponse(Player player)
    {
        this.id = player.getId();
        this.name = player.getName();
        this.dateOfBirth = player.getDateOfBirth();
        this.image = player.getImage();
    }
}
