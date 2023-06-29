package com.springboot.cric.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FielderDismissalCustomRepository extends BaseCustomRepository {
    public Map<String, Map<String, Integer>> getFieldingStats(Long playerId)
    {
        Map<String, Map<String, Integer>> statsFinal = new HashMap<>();

        String query = "select dm.name as dismissalMode, count(*) as count, gt.name as gameType from fielder_dismissals fd inner join match_player_map mpm on mpm.id = fd.match_player_id inner join batting_scores bs on bs.id = fd.score_id and mpm.player_id = " + playerId + " inner join dismissal_modes dm on dm.id = bs.dismissal_mode_id inner join matches m on m.id = mpm.match_id and m.is_official = true inner join series s on s.id = m.series_id inner join teams t on t.id = mpm.team_id inner join team_types tt on tt.id = t.type_id and tt.name = 'International' inner join game_types gt on gt.id = s.game_type_id group by gt.name, dm.name";
        List<Map<String, Object>> result = executeRawQuery(query);

        for(Map<String, Object> row: result)
        {
            String gameType = row.get("gametype").toString();
            if(statsFinal.containsKey(gameType))
            {
                statsFinal.get(gameType).put(row.get("dismissalmode").toString(), Integer.parseInt(row.get("count").toString()));
            }
            else
            {
                statsFinal.put(gameType, new HashMap<>(){
                    {
                        put(row.get("dismissalmode").toString(), Integer.parseInt(row.get("count").toString()));
                    }
                });
            }
        }

        return statsFinal;
    }
}
