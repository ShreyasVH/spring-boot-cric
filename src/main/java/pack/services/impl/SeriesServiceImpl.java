package pack.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pack.models.db.*;
import pack.models.requests.series.CreateRequest;
import pack.models.responses.ManOfTheSeriesResponse;
import pack.models.responses.SeriesResponse;
import pack.repositories.*;
import pack.services.SeriesService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesServiceImpl implements SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeriesTeamsMapRepository seriesTeamsMapRepository;

    @Autowired
    private SeriesTypeRepository seriesTypeRepository;

    @Autowired
    private GameTypeRepository gameTypeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ManOfTheSeriesRepository manOfTheSeriesRepository;

    @Autowired
    private MatchRepository matchRepository;

    public SeriesResponse seriesResponse(Series series)
    {
        SeriesResponse seriesResponse = new SeriesResponse(series);

        seriesResponse.setHomeCountry(countryRepository.findById(series.getHomeCountryId()).orElse(null));
        seriesResponse.setTeams(teamRepository.findAllById(seriesTeamsMapRepository.findAllBySeriesId(series.getId()).stream().map(SeriesTeamsMap::getTeamId).collect(Collectors.toList())));
        List<ManOfTheSeries> manOfTheSeriesList = manOfTheSeriesRepository.findAllBySeriesId(series.getId());
        for(ManOfTheSeries mots: manOfTheSeriesList)
        {
            ManOfTheSeriesResponse motsResponse = new ManOfTheSeriesResponse(mots);
            Team team = teamRepository.findById(mots.getTeamId()).orElse(null);
            if(null == team)
            {
//                throw new NotFoundException(ErrorCode.NOT_FOUND.getCode(), String.format(ErrorCode.NOT_FOUND.getDescription(), "Team"));
            }
            motsResponse.setTeamName(team.getName());

            Player player = playerRepository.findById(mots.getPlayerId()).orElse(null);
            motsResponse.setPlayerName(player.getName());

            seriesResponse.getManOfTheSeriesList().add(motsResponse);
        }

        seriesResponse.setMatches(matchRepository.findAllBySeries(series.getId()));

        return seriesResponse;
    }

    @Override
    @Transactional
    public Series create(CreateRequest createRequest) {
        Series series = new Series(createRequest);
//        series.setGameTypeId(gameTypeRepository.findByName(createRequest.getGameType()).getId());
//        series.setTypeId(seriesTypeRepository.findByName(createRequest.getType()).getId());
        Series createdSeries = seriesRepository.save(series);

        List<SeriesTeamsMap> teamMaps = createRequest.getTeams().stream().map(teamId -> new SeriesTeamsMap(null, createdSeries.getId(), teamId)).collect(Collectors.toList());
        seriesTeamsMapRepository.saveAll(teamMaps);

        return createdSeries;
    }

    @Override
    public List<Series> getAll() {
        return seriesRepository.findAll();
    }

    @Override
    public SeriesResponse get(Long id) {
        return seriesResponse(seriesRepository.findById(id).orElse(null));
    }
}
