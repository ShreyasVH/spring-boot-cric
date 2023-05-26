package com.springboot.cric.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import com.springboot.cric.requests.stadiums.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "stadiums")
public class Stadium
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String state;
    private Long countryId;

    public Stadium(CreateRequest createRequest) {
        this.name = createRequest.getName();
        this.city = createRequest.getCity();
        this.state = createRequest.getState();
        this.countryId = createRequest.getCountryId();
    }
}