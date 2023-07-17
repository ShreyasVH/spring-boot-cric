package com.springboot.cric.repositories;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TourCustomRepository extends BaseCustomRepository {
    public List<Integer> getAllYears() {
        List<Integer> years = new ArrayList<>();

        String query = "SELECT DISTINCT EXTRACT(YEAR FROM start_time) AS year FROM tours ORDER BY year DESC";
        List<Map<String, Object>> result = executeRawQuery(query);

        for (Map<String, Object> row: result) {
            years.add(Integer.parseInt(row.get("year").toString()));
        }

        return years;
    }
}
