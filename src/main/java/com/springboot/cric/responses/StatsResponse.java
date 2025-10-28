package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class StatsResponse {
    private Integer count = 0;
    private List<Map<String, String>> stats = new ArrayList<>();
}
