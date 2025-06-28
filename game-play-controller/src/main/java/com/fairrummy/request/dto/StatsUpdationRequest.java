package com.fairrummy.request.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatsUpdationRequest {
    private long playerId;
    private double skill;
}
