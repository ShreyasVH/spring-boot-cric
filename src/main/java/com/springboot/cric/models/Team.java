package com.springboot.cric.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import com.springboot.cric.requests.teams.CreateRequest;

@Data
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long countryId;
    private Integer typeId;

    public Team(CreateRequest createRequest) {
        this.name = createRequest.getName();
        this.countryId = createRequest.getCountryId();
        this.typeId = createRequest.getTypeId();
    }
}