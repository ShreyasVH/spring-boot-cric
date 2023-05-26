package com.springboot.cric.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import com.springboot.cric.requests.countries.CreateRequest;

@Data
@Entity
@NoArgsConstructor
@Table(name = "countries")
public class Country
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Country(CreateRequest createRequest) {
        this.name = createRequest.getName();
    }
}