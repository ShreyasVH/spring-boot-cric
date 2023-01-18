package pack.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.models.db.Stadium;
import pack.models.requests.stadiums.CreateRequest;
import pack.repositories.StadiumRepository;
import pack.services.StadiumService;

import java.util.List;

@Service
public class StadiumServiceImpl implements StadiumService {
    @Autowired
    private StadiumRepository stadiumRepository;

    @Override
    public Stadium create(CreateRequest request) {
        Stadium stadium = new Stadium(request);
        return stadiumRepository.save(stadium);
    }

    @Override
    public List<Stadium> getAll() {
        return stadiumRepository.findAll();
    }
}
