package com.springboot.cric.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository
public class BattingScoreCustomRepository extends BaseCustomRepository {
    public Map<String, Map<String, Integer>> getDismissalStats(Long playerId)
    {
        Map<String, Map<String, Integer>> stats = new HashMap<>();
        String query = "SELECT dm.name AS dismissalMode, COUNT(*) AS count, gt.name as gameType FROM batting_scores bs INNER JOIN match_player_map mpm on mpm.id = bs.match_player_id inner join dismissal_modes dm ON mpm.player_id = " + playerId + " AND bs.dismissal_mode_id IS NOT NULL and dm.id = bs.dismissal_mode_id and dm.name != 'Retired Hurt' inner join matches m on m.id = mpm.match_id and m.is_official = true inner join series s on s.id = m.series_id inner join teams t on t.id = mpm.team_id inner join team_types tt on tt.id = t.type_id and tt.name = 'International' inner join game_types gt on gt.id = s.game_type_id GROUP BY gt.name, dm.name";
        List<Map<String, Object>> result = executeRawQuery(query);

        for(Map<String, Object> row: result)
        {
            String gameType = row.get("gametype".toUpperCase()).toString();
            if(stats.containsKey(gameType))
            {
                stats.get(gameType).put(row.get("dismissalmode".toUpperCase()).toString(), Integer.parseInt(row.get("count".toUpperCase()).toString()));
            }
            else
            {
                Map<String, Integer> partStats = new HashMap<>(){
                    {
                        put(row.get("dismissalmode".toUpperCase()).toString(), Integer.parseInt(row.get("count".toUpperCase()).toString()));
                    }
                };
                stats.put(gameType, partStats);
            }
        }


        return stats;
    }

    public Map<String, Map<String, Integer>> getBattingStats(Long playerId)
    {
        Map<String, Map<String, Integer>> statsFinal = new HashMap<>();
        String query = "SELECT COUNT(*) AS innings, SUM(runs) AS runs, SUM(balls) AS balls, SUM(fours) AS fours, SUM(sixes) AS sixes, MAX(runs) AS highest, gt.name as gameType, count(CASE WHEN (bs.runs >= 50 and bs.runs < 100) then 1 end) as fifties, count(CASE WHEN (bs.runs >= 100 and bs.runs < 200) then 1 end) as hundreds, count(CASE WHEN (bs.runs >= 200 and bs.runs < 300) then 1 end) as twoHundreds, count(CASE WHEN (bs.runs >= 300 and bs.runs < 400) then 1 end) as threeHundreds, count(CASE WHEN (bs.runs >= 400 and bs.runs < 500) then 1 end) as fourHundreds FROM batting_scores bs inner join match_player_map mpm on mpm.player_id = " + playerId + " and  mpm.id = bs.match_player_id inner join matches m on m.id = mpm.match_id and m.is_official = true inner join series s on s.id = m.series_id inner join teams t on t.id = mpm.team_id inner join team_types tt on tt.id = t.type_id and tt.name = 'International' inner join game_types gt on gt.id = s.game_type_id group by gt.name";
        List<Map<String, Object>> result = executeRawQuery(query);

        for(Map<String, Object> row: result)
        {
            int innings = Integer.parseInt(row.get("innings".toUpperCase()).toString());
            if(innings > 0)
            {
                Map<String, Integer> stats = new HashMap<>();

                stats.put("innings", innings);
                stats.put("runs", Integer.parseInt(row.get("runs".toUpperCase()).toString()));
                stats.put("balls", Integer.parseInt(row.get("balls".toUpperCase()).toString()));
                stats.put("fours", Integer.parseInt(row.get("fours".toUpperCase()).toString()));
                stats.put("sixes", Integer.parseInt(row.get("sixes".toUpperCase()).toString()));
                stats.put("highest", Integer.parseInt(row.get("highest".toUpperCase()).toString()));
                stats.put("fifties", Integer.parseInt(row.get("fifties".toUpperCase()).toString()));
                stats.put("hundreds", Integer.parseInt(row.get("hundreds".toUpperCase()).toString()));
                stats.put("twoHundreds", Integer.parseInt(row.get("twohundreds".toUpperCase()).toString()));
                stats.put("threeHundreds", Integer.parseInt(row.get("threehundreds".toUpperCase()).toString()));
                stats.put("fourHundreds", Integer.parseInt(row.get("fourhundreds".toUpperCase()).toString()));

                String gameType = row.get("gametype".toUpperCase()).toString();
                statsFinal.put(gameType, stats);
            }
        }

        return statsFinal;
    }
}
