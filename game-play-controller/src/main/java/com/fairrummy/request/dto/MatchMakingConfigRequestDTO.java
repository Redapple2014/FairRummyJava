package com.fairrummy.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchMakingConfigRequestDTO {
    @NotEmpty
    private List<String> templateIds;
    @NotEmpty
    private String gameFormat;
    @NotNull
    private Long searchTimeout;
    @NotEmpty
    private Map<Integer, Long> skillVsWaitTime;
    @NotNull
    private Boolean isActive;
}
