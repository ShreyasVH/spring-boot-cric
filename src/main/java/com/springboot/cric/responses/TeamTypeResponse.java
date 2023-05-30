package com.springboot.cric.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.springboot.cric.models.TeamType;

@Data
@NoArgsConstructor
public class TeamTypeResponse {
    private Integer id;
    private String name;

    public TeamTypeResponse(TeamType teamType) {
        this.id = teamType.getId();
        this.name = teamType.getName();
    }
}
