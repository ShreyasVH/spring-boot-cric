package com.springboot.cric.requests.players;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MergeRequest {
    private Long playerIdToMerge;
    private Long originalPlayerId;
}
