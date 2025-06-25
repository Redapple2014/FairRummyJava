package org.fcesur.gcs.service;

import org.fcesur.gcs.exception.MatchMakingConfigBadRequestException;
import org.fcesur.gcs.request.dto.MatchMakingConfigRequestDTO;
import org.fcesur.gcs.request.dto.MatchMakingConfigResponseDTO;

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
