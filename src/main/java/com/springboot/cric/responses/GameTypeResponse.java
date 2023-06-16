package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.GameType;

@Data
@NoArgsConstructor
public class GameTypeResponse {
    private Integer id;
    private String name;

    public GameTypeResponse(GameType gameType) {
        this.id = gameType.getId();
        this.name = gameType.getName();
    }
}
