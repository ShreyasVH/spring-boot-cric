package com.springboot.cric.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.springboot.cric.requests.series.CreateRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Series(CreateRequest createRequest) {
        this.name = createRequest.getName();
        this.homeCountryId = createRequest.getHomeCountryId();
        this.tourId = createRequest.getTourId();
        this.typeId = createRequest.getTypeId();
        this.gameTypeId = createRequest.getGameTypeId();
        this.startTime = createRequest.getStartTime();
    }
}