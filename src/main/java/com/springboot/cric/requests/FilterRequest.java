package com.springboot.cric.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FilterRequest {
    private String type;
    private Integer offset = 0;
    private Integer count = 30;
    private Map<String, List<String>> filters = new HashMap<>();
    private Map<String, Map<String, String>> rangeFilters = new HashMap<>();
    private Map<String, String> sortMap = new HashMap<>();
}
