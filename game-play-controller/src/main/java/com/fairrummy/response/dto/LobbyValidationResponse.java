package com.fairrummy.response.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LobbyValidationResponse {
    private boolean valid;
    private String activeGameTemplateId;
    private Long activeGameRoomId;
    private Integer errorCode;
    private String errMsg;
    private String socketUrl;

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
