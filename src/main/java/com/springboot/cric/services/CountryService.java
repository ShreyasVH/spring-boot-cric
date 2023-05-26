package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.springboot.cric.models.Country;
import com.springboot.cric.repositories.CountryRepository;
import com.springboot.cric.requests.countries.CreateRequest;
import com.springboot.cric.exceptions.ConflictException;

@Service
public class CountryService {
	@Autowired
	private CountryRepository countryRepository;

	public Country create(CreateRequest createRequest) {
		createRequest.validate();

		Country existingCountry = countryRepository.findByName(createRequest.getName());
		if(null != existingCountry) {
			throw new ConflictException("Country");
		}

		Country country = new Country(createRequest);
		return countryRepository.save(country);
	}

	public List<Country> searchByName(String name) {
		return countryRepository.findByNameContainingIgnoringCase(name);
	}
}