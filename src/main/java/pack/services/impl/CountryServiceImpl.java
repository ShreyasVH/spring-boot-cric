package pack.services.impl;

import pack.models.db.Country;
import pack.models.requests.countries.CreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.repositories.CountryRepository;
import pack.services.CountryService;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService
{
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Country create(CreateRequest request) {
        Country country = new Country(request);
        return countryRepository.save(country);
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }
}
