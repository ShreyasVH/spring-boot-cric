package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.Country;

@Data
@NoArgsConstructor
public class CountryResponse {
	private Long id;
	private String name;

	public CountryResponse(Country country) {
		this.id = country.getId();
		this.name = country.getName();
	}
}