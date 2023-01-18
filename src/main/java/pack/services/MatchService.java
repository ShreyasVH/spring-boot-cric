package pack.services;

import pack.models.db.Match;
import pack.models.requests.matches.CreateRequest;
import pack.models.responses.MatchResponse;

public interface MatchService {
    Match create(CreateRequest createRequest);
    MatchResponse get(Long id);
}
