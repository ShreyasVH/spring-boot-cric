package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.Stadium;

@Data
@NoArgsConstructor
public class StadiumResponse {
    private Long id;
    private String name;
    private String city;
    private String state;
    private CountryResponse country;

    public StadiumResponse(Stadium stadium, CountryResponse country) {
        this.id = stadium.getId();
        this.name = stadium.getName();
        this.city = stadium.getCity();
        this.state = stadium.getState();
        this.country = country;
    }
}