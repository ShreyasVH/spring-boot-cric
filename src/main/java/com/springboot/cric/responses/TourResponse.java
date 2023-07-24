package com.springboot.cric.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.Tour;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TourResponse {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;
    private List<SeriesMiniResponse> seriesList;

    public TourResponse(Tour tour) {
        this.id = tour.getId();
        this.name = tour.getName();
        this.startTime = tour.getStartTime();
    }
}