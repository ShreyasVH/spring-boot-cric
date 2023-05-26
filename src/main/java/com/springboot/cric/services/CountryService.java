package com.springboot.cric.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

	public List<Country> getAll(int page, int limit) {
		Sort sort = Sort.by(Sort.Direction.fromString("asc"), "name");
		PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
		Page<Country> countriesPage = countryRepository.findAll(pageRequest);
		return countriesPage.getContent();
	}

	public long getTotalCount() {
		return countryRepository.count();
	}

	public Country getById(Long id) {
		return countryRepository.findById(id).orElse(null);
	}

	public List<Country> getByIds(List<Long> ids) {
		return countryRepository.findByIdIn(ids);
	}
}