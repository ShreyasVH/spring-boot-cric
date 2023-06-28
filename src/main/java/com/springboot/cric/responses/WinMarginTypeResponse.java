package com.springboot.cric.responses;

import com.springboot.cric.models.WinMarginType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WinMarginTypeResponse {
    private Integer id;
    private String name;

    public WinMarginTypeResponse(WinMarginType winMarginType)
    {
        this.id = winMarginType.getId();
        this.name = winMarginType.getName();
    }
}
