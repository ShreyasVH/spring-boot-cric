package com.springboot.cric.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.requests.players.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "players")
public class Player
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long countryId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    private String image;

    public Player(CreateRequest createRequest) {
        this.name = createRequest.getName();
        this.countryId = createRequest.getCountryId();
        this.dateOfBirth = createRequest.getDateOfBirth();
        this.image = createRequest.getImage();
    }
}