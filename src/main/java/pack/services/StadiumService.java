package pack.services;

import pack.models.db.Stadium;
import pack.models.requests.stadiums.CreateRequest;

import java.util.List;

public interface StadiumService {
    Stadium create(CreateRequest request);
    List<Stadium> getAll();
}
