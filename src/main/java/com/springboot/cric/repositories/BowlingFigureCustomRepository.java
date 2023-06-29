package com.springboot.cric.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BowlingFigureCustomRepository extends BaseCustomRepository {
    public Map<String, Map<String, Integer>> getBasicBowlingStats(Long playerId)
    {
        Map<String, Map<String, Integer>> statsFinal = new HashMap<>();

        String query = "SELECT COUNT(*) AS innings, SUM(balls) AS balls, SUM(maidens) AS maidens, SUM(runs) AS runs, SUM(wickets) AS wickets, gt.name AS gameType, COUNT(CASE WHEN (bf.wickets >= 5 and bf.wickets < 10) then 1 end) as fifers,  COUNT(CASE WHEN (bf.wickets = 10) then 1 end) as tenWickets FROM bowling_figures bf inner join match_player_map mpm on mpm.id = bf.match_player_id and mpm.player_id = " + playerId + " INNER JOIN matches m ON m.id = mpm.match_id INNER JOIN series s ON s.id = m.series_id and m.is_official = true inner join teams t on t.id = mpm.team_id inner join team_types tt on tt.id = t.type_id and tt.name = 'International' inner join game_types gt on gt.id = s.game_type_id GROUP BY gt.name";
        List<Map<String, Object>> result = executeRawQuery(query);

        for(Map<String, Object> row: result)
        {
            String gameType = row.get("gametype").toString();
            int innings = Integer.parseInt(row.get("innings").toString());
            if(innings > 0)
            {
                Map<String, Integer> stats = new HashMap<>();

                stats.put("innings", innings);
                stats.put("runs", Integer.parseInt(row.get("runs").toString()));
                stats.put("balls", Integer.parseInt(row.get("balls").toString()));
                stats.put("maidens", Integer.parseInt(row.get("maidens").toString()));
                stats.put("wickets", Integer.parseInt(row.get("wickets").toString()));
                stats.put("fifers", Integer.parseInt(row.get("fifers").toString()));
                stats.put("tenWickets", Integer.parseInt(row.get("tenwickets").toString()));

                statsFinal.put(gameType, stats);
            }
        }

        return statsFinal;
    }
}
