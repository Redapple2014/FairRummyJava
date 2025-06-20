package com.skillengine.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TableStatusInfo {
    private long tableId;
    private String engineIP;
    private int templateId;
    private int maxPlayer;
    private int availableSeats;
    private int status;
}

