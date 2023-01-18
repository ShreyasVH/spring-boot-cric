package pack.services;

import pack.models.db.Country;
import pack.models.requests.countries.CreateRequest;

import java.util.List;

public interface CountryService
{
    Country create(CreateRequest request);
    List<Country> getAll();
}
