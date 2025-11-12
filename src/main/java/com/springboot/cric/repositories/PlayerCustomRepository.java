package com.springboot.cric.repositories;

import com.springboot.cric.requests.FilterRequest;
import com.springboot.cric.responses.StatsResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlayerCustomRepository extends BaseCustomRepository {
    public String getFieldNameForDisplay(String field)
    {
        String fieldName = "";

        switch(field)
        {
            case "runs":
                fieldName = "runs";
                break;
            case "balls":
                fieldName = "balls";
                break;
            case "innings":
                fieldName = "innings";
                break;
            case "notOuts":
                fieldName = "notouts";
                break;
            case "fifties":
                fieldName = "fifties";
                break;
            case "hundreds":
                fieldName = "hundreds";
                break;
            case "highest":
                fieldName = "highest";
                break;
            case "fours":
                fieldName = "fours";
                break;
            case "sixes":
                fieldName = "sixes";
                break;
            case "wickets":
                fieldName = "wickets";
                break;
            case "maidens":
                fieldName = "maidens";
                break;
            case "fifers":
                fieldName = "fifers";
                break;
            case "tenWickets":
                fieldName = "tenWickets";
                break;
            case "fielderCatches":
                fieldName = "fielderCatches";
                break;
            case "keeperCatches":
                fieldName = "keeperCatches";
                break;
            case "stumpings":
                fieldName = "stumpings";
                break;
            case "runOuts":
                fieldName = "runOuts";
                break;
        }

        return fieldName;
    }

    public String getFieldNameWithTablePrefix(String field)
    {
        String fieldName = "";

        switch(field)
        {
            case "gameType":
                fieldName = "s.game_type_id";
                break;
            case "stadium":
                fieldName = "m.stadium_id";
                break;
            case "team":
                fieldName = "t.id";
                break;
            case "opposingTeam":
                fieldName = "(CASE WHEN t.id = m.team_1_id THEN m.team_2_id ELSE m.team_1_id END)";
                break;
            case "teamType":
                fieldName = "t.type_id";
                break;
            case "country":
                fieldName = "p.country_id";
                break;
            case "series":
                fieldName = "s.id";
                break;
            case "year":
                fieldName = "EXTRACT(YEAR FROM m.start_time)";
                break;
            case "playerName":
                fieldName = "p.name";
                break;
        }

        return fieldName;
    }

    public StatsResponse getBattingStats(FilterRequest filterRequest) {
        StatsResponse statsResponse = new StatsResponse();
        List<Map<String, String>> statList = new ArrayList<>();
        String query = "select p.id as playerId, p.name AS name, sum(bs.runs) AS runs, count(0) AS innings, sum(bs.balls) AS balls, sum(bs.fours) AS fours, sum(bs.sixes) AS sixes, max(bs.runs) AS highest, count((case when (bs.dismissal_mode_id is null) then 1 end)) AS notouts, count((case when ((bs.runs >= 50) and (bs.runs < 100)) then 1 end)) AS fifties, count((case when ((bs.runs >= 100)) then 1 end)) AS hundreds from batting_scores bs " +
                "inner join match_player_map mpm on mpm.id = bs.match_player_id " +
                "inner join players p on p.id = mpm.player_id " +
                "inner join matches m on m.id = mpm.match_id " +
                "inner join series s on s.id = m.series_id " +
                "inner join stadiums st on st.id = m.stadium_id " +
                "inner join teams t on t.id = mpm.team_id";

        String countQuery = "select count(distinct p.id) as count from batting_scores bs " +
                "inner join match_player_map mpm on mpm.id = bs.match_player_id " +
                "inner join players p on p.id = mpm.player_id " +
                "inner join matches m on m.id = mpm.match_id " +
                "inner join series s on s.id = m.series_id " +
                "inner join stadiums st on st.id = m.stadium_id " +
                "inner join teams t on t.id = mpm.team_id";

        //where
        List<String> whereQueryParts = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : filterRequest.getFilters().entrySet()) {
            String field = entry.getKey();
            List<String> valueList = entry.getValue();

            String fieldNameWithTablePrefix = getFieldNameWithTablePrefix(field);
            if (!fieldNameWithTablePrefix.isEmpty() && !valueList.isEmpty()) {
                whereQueryParts.add(fieldNameWithTablePrefix + " in (" + String.join(", ", valueList) + ")");
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : filterRequest.getRangeFilters().entrySet()) {
            String field = entry.getKey();
            Map<String, String> rangeValues = entry.getValue();

            String fieldNameWithTablePrefix = getFieldNameWithTablePrefix(field);
            if (!fieldNameWithTablePrefix.isEmpty() && !rangeValues.isEmpty()) {
                if (rangeValues.containsKey("from")) {
                    whereQueryParts.add(fieldNameWithTablePrefix + " >= " + rangeValues.get("from"));
                }
                if (rangeValues.containsKey("to")) {
                    whereQueryParts.add(fieldNameWithTablePrefix + " <= " + rangeValues.get("to"));
                }

            }
        }

        if (!whereQueryParts.isEmpty()) {
            query += " where " + String.join(" and ", whereQueryParts);
            countQuery += " where " + String.join(" and ", whereQueryParts);
        }

        query += " group by playerId";

        //sort
        List<String> sortList = new ArrayList<>();
        for (Map.Entry<String, String> entry : filterRequest.getSortMap().entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            String sortFieldName = getFieldNameForDisplay(field);
            if (!sortFieldName.isEmpty()) {
                sortList.add(sortFieldName + " " + value);
            }
        }
        if (sortList.isEmpty()) {
            sortList.add(getFieldNameForDisplay("runs") + " desc");
        }
        query += " order by " + String.join(", ", sortList);

        //offset limit
        query += " limit " + Integer.min(30, filterRequest.getCount()) + " offset " + filterRequest.getOffset();

        List<Map<String, Object>> countResult = executeRawQuery(countQuery);
        statsResponse.setCount(Long.parseLong(countResult.get(0).get("count").toString()));

        List<Map<String, Object>> result = executeRawQuery(query);
        for(Map<String, Object> row: result)
        {
            long innings = Long.parseLong(row.get("innings").toString());
            if (innings > 0L) {
                Map<String, String> stats = new HashMap<>();

                stats.put("id", row.get("playerid").toString());
                stats.put("name", row.get("name").toString());
                stats.put("innings", String.valueOf(innings));
                stats.put("runs", row.get("runs").toString());
                stats.put("balls", row.get("balls").toString());
                stats.put("notOuts", row.get("notouts").toString());
                stats.put("fours", row.get("fours").toString());
                stats.put("sixes", row.get("sixes").toString());
                stats.put("highest", row.get("highest").toString());
                stats.put("fifties", row.get("fifties").toString());
                stats.put("hundreds", row.get("hundreds").toString());
//                stats.put("twoHundreds", row.get("twoHundreds").toString());
//                stats.put("threeHundreds", row.get("threeHundreds").toString());
//                stats.put("fourHundreds", row.get("fourHundreds").toString());
//
                statList.add(stats);
            }
        }

        statsResponse.setStats(statList);

        return statsResponse;
    }

    public StatsResponse getBowlingStats(FilterRequest filterRequest) {
        StatsResponse statsResponse = new StatsResponse();
        List<Map<String, String>> statList = new ArrayList<>();
        String query = "select p.id as playerId, p.name AS name, sum(bf.wickets) AS wickets, sum(bf.runs) as runs, count(0) AS innings, sum(bf.balls) AS balls, sum(bf.maidens) AS maidens, count((case when ((bf.wickets >= 5) and (bf.wickets < 10)) then 1 end)) AS fifers, count((case when ((bf.wickets = 10)) then 1 end)) AS tenWickets from bowling_figures bf " +
                "inner join match_player_map mpm on mpm.id = bf.match_player_id " +
                "inner join players p on p.id = mpm.player_id " +
                "inner join matches m on m.id = mpm.match_id " +
                "inner join series s on s.id = m.series_id " +
                "inner join stadiums st on st.id = m.stadium_id " +
                "inner join teams t on t.id = mpm.team_id";

        String countQuery = "select count(distinct p.id) as count from bowling_figures bf " +
                "inner join match_player_map mpm on mpm.id = bf.match_player_id " +
                "inner join players p on p.id = mpm.player_id " +
                "inner join matches m on m.id = mpm.match_id " +
                "inner join series s on s.id = m.series_id " +
                "inner join stadiums st on st.id = m.stadium_id " +
                "inner join teams t on t.id = mpm.team_id";

        //where
        List<String> whereQueryParts = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : filterRequest.getFilters().entrySet()) {
            String field = entry.getKey();
            List<String> valueList = entry.getValue();

            String fieldNameWithTablePrefix = getFieldNameWithTablePrefix(field);
            if (!fieldNameWithTablePrefix.isEmpty() && !valueList.isEmpty()) {
                whereQueryParts.add(fieldNameWithTablePrefix + " in (" + String.join(", ", valueList) + ")");
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : filterRequest.getRangeFilters().entrySet()) {
            String field = entry.getKey();
            Map<String, String> rangeValues = entry.getValue();

            String fieldNameWithTablePrefix = getFieldNameWithTablePrefix(field);
            if (!fieldNameWithTablePrefix.isEmpty() && !rangeValues.isEmpty()) {
                if (rangeValues.containsKey("from")) {
                    whereQueryParts.add(fieldNameWithTablePrefix + " >= " + rangeValues.get("from"));
                }
                if (rangeValues.containsKey("to")) {
                    whereQueryParts.add(fieldNameWithTablePrefix + " <= " + rangeValues.get("to"));
                }

            }
        }

        if (!whereQueryParts.isEmpty()) {
            query += " where " + String.join(" and ", whereQueryParts);
            countQuery += " where " + String.join(" and ", whereQueryParts);
        }

        query += " group by playerId";

        //sort
        List<String> sortList = new ArrayList<>();
        for (Map.Entry<String, String> entry : filterRequest.getSortMap().entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            String sortFieldName = getFieldNameForDisplay(field);
            if (!sortFieldName.isEmpty()) {
                sortList.add(sortFieldName + " " + value);
            }
        }
        if (sortList.isEmpty()) {
            sortList.add(getFieldNameForDisplay("wickets") + " desc");
        }
        query += " order by " + String.join(", ", sortList);

        //offset limit
        query += " limit " + Integer.min(30, filterRequest.getCount()) + " offset " + filterRequest.getOffset();

        List<Map<String, Object>> countResult = executeRawQuery(countQuery);
        statsResponse.setCount(Long.parseLong(countResult.get(0).get("count").toString()));

        List<Map<String, Object>> result = executeRawQuery(query);
        for(Map<String, Object> row: result)
        {
            long innings = Long.parseLong(row.get("innings").toString());
            if (innings > 0L) {
                Map<String, String> stats = new HashMap<>();

                stats.put("id", row.get("playerid").toString());
                stats.put("name", row.get("name").toString());
                stats.put("innings", String.valueOf(innings));
                stats.put("wickets", row.get("wickets").toString());
                stats.put("runs", row.get("runs").toString());
                stats.put("balls", row.get("balls").toString());
                stats.put("maidens", row.get("maidens").toString());
                stats.put("fifers", row.get("fifers").toString());
                stats.put("tenWickets", row.get("tenwickets").toString());

                statList.add(stats);
            }
        }

        statsResponse.setStats(statList);

        return statsResponse;
    }

    public StatsResponse getFieldingStats(FilterRequest filterRequest) {
        StatsResponse statsResponse = new StatsResponse();
        List<Map<String, String>> statList = new ArrayList<>();
        String query = "select p.id as playerId, p.name AS name, SUM(case when dm.name = 'Caught' and wk.id is null then 1 else 0 end) as fielderCatches, SUM(case when dm.name = 'Caught' and wk.id is not null then 1 else 0 end) as keeperCatches, SUM(case when dm.name = 'Stumped' then 1 else 0 end) as stumpings, SUM(case when dm.name = 'Run Out' then 1 else 0 end) as runOuts from fielder_dismissals fd " +
                "inner join match_player_map mpm on mpm.id = fd.match_player_id " +
                "inner join players p on p.id = mpm.player_id " +
                "inner join matches m on m.id = mpm.match_id " +
                "inner join series s on s.id = m.series_id " +
                "inner join stadiums st on st.id = m.stadium_id " +
                "inner join batting_scores bs on bs.id = fd.score_id " +
                "inner join dismissal_modes dm on dm.id = bs.dismissal_mode_id " +
                "inner join teams t on t.id = mpm.team_id " +
                "left join wicket_keepers wk on wk.match_player_id = fd.match_player_id";

        String countQuery = "select count(distinct p.id) as count from fielder_dismissals fd " +
                "inner join match_player_map mpm on mpm.id = fd.match_player_id " +
                "inner join players p on p.id = mpm.player_id " +
                "inner join matches m on m.id = mpm.match_id " +
                "inner join series s on s.id = m.series_id " +
                "inner join stadiums st on st.id = m.stadium_id " +
                "inner join batting_scores bs on bs.id = fd.score_id " +
                "inner join dismissal_modes dm on dm.id = bs.dismissal_mode_id " +
                "inner join teams t on t.id = mpm.team_id " +
                "left join wicket_keepers wk on wk.match_player_id = fd.match_player_id";

        //where
        List<String> whereQueryParts = new ArrayList<>(){
            {
                add(getFieldNameWithTablePrefix("playerName") + " != 'sub'");
            }
        };
        for (Map.Entry<String, List<String>> entry : filterRequest.getFilters().entrySet()) {
            String field = entry.getKey();
            List<String> valueList = entry.getValue();

            String fieldNameWithTablePrefix = getFieldNameWithTablePrefix(field);
            if (!fieldNameWithTablePrefix.isEmpty() && !valueList.isEmpty()) {
                whereQueryParts.add(fieldNameWithTablePrefix + " in (" + String.join(", ", valueList) + ")");
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : filterRequest.getRangeFilters().entrySet()) {
            String field = entry.getKey();
            Map<String, String> rangeValues = entry.getValue();

            String fieldNameWithTablePrefix = getFieldNameWithTablePrefix(field);
            if (!fieldNameWithTablePrefix.isEmpty() && !rangeValues.isEmpty()) {
                if (rangeValues.containsKey("from")) {
                    whereQueryParts.add(fieldNameWithTablePrefix + " >= " + rangeValues.get("from"));
                }
                if (rangeValues.containsKey("to")) {
                    whereQueryParts.add(fieldNameWithTablePrefix + " <= " + rangeValues.get("to"));
                }

            }
        }

        if (!whereQueryParts.isEmpty()) {
            query += " where " + String.join(" and ", whereQueryParts);
            countQuery += " where " + String.join(" and ", whereQueryParts);
        }

        query += " group by playerId";

        //sort
        List<String> sortList = new ArrayList<>();
        for (Map.Entry<String, String> entry : filterRequest.getSortMap().entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            String sortFieldName = getFieldNameForDisplay(field);
            if (!sortFieldName.isEmpty()) {
                sortList.add(sortFieldName + " " + value);
            }
        }
        if (sortList.isEmpty()) {
            sortList.add(getFieldNameForDisplay("fielderCatches") + " desc");
        }
        query += " order by " + String.join(", ", sortList);

        //offset limit
        query += " limit " + Integer.min(30, filterRequest.getCount()) + " offset " + filterRequest.getOffset();

        List<Map<String, Object>> countResult = executeRawQuery(countQuery);
        statsResponse.setCount(Long.parseLong(countResult.get(0).get("count").toString()));

        List<Map<String, Object>> result = executeRawQuery(query);
        for(Map<String, Object> row: result)
        {
            Map<String, String> stats = new HashMap<>();

            stats.put("id", row.get("playerid").toString());
            stats.put("name", row.get("name").toString());
            stats.put("fielderCatches", row.get("fieldercatches").toString());
            stats.put("keeperCatches", row.get("keepercatches").toString());
            stats.put("stumpings", row.get("stumpings").toString());
            stats.put("runOuts", row.get("runouts").toString());

            statList.add(stats);
        }

        statsResponse.setStats(statList);

        return statsResponse;
    }
}
