package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.SeriesType;

@Data
@NoArgsConstructor
public class SeriesTypeResponse {
    private Integer id;
    private String name;

    public SeriesTypeResponse(SeriesType seriesType) {
        this.id = seriesType.getId();
        this.name = seriesType.getName();
    }
}
