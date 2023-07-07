package com.springboot.cric.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.cric.models.Series;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SeriesMiniResponse {
    private Long id;
    private String name;
    private Long homeCountryId;
    private Long tourId;
    private Integer typeId;
    private Integer gameTypeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    public SeriesMiniResponse(Series series)
    {
        this.id = series.getId();
        this.name = series.getName();
        this.homeCountryId = series.getHomeCountryId();
        this.tourId = series.getTourId();
        this.typeId = series.getTypeId();
        this.gameTypeId = series.getGameTypeId();
        this.startTime = series.getStartTime();
    }
}
