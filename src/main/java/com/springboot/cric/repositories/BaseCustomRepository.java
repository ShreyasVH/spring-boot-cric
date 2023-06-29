package com.springboot.cric.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCustomRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> executeRawQuery(String sql) {
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            int columnCount = resultSet.getMetaData().getColumnCount();
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < columnCount; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
                result.put(columnName, resultSet.getObject(i + 1));
            }
            return result;
        });
    }
}
