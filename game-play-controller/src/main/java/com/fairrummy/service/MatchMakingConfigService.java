package com.fairrummy.service;

import com.fairrummy.exception.MatchMakingConfigBadRequestException;
import com.fairrummy.request.dto.MatchMakingConfigRequestDTO;
import com.fairrummy.request.dto.MatchMakingConfigResponseDTO;

import java.util.List;

public interface MatchMakingConfigService {
    MatchMakingConfigResponseDTO createMatchMakingConfig(
          MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
          throws MatchMakingConfigBadRequestException;

    MatchMakingConfigResponseDTO updateMatchMakingConfig(
          String matchMakingConfigId, MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
          throws MatchMakingConfigBadRequestException;

    MatchMakingConfigResponseDTO getMatchMakingConfig(String configId)
          throws MatchMakingConfigBadRequestException;

    List<MatchMakingConfigResponseDTO> getAllMatchMakingConfig()
          throws MatchMakingConfigBadRequestException;

    MatchMakingConfigResponseDTO getMatchMakingConfigByTemplateId(String templateId);
}
