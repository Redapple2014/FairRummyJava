package com.fairrummy.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchMakingConfigResponseDTO {
    private String matchMakingConfigId;
    private String gameFormat;
    private List<String> templateIds;
    private Long searchTimeout;
    private Map<Integer, Long> skillVsWaitTime;
    private Boolean isActive;
}
