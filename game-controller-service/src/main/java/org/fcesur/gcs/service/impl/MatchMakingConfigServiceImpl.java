package org.fcesur.gcs.service.impl;

/*import static com.fairrummy.constants.ErrorCodes.UNKNOWN_ERROR;
import static com.fairrummy.constants.ErrorCodes.MATCH_MAKING_CONFIG_NOT_FOUND;
import static com.fairrummy.constants.ErrorCodes.INVALID_TEMPLATE_IDS_FOR_MATCH_MAKING_CONFIG;
import static com.fairrummy.constants.ErrorCodes.DUPLICATE_MATCHMAKING_CONFIG_FOR_TEMPLATE;

import com.fairrummy.dao.MatchMakingConfigDao;
import com.fairrummy.exception.MatchMakingConfigBadRequestException;
import com.fairrummy.exception.MatchMakingConfigInternalServerException;
import com.fairrummy.http.impl.MatchMakingServiceRestClient;
import com.fairrummy.mapper.PCObjectMapper;
import com.fairrummy.model.entity.MatchMakingConfig;
import com.fairrummy.model.entity.RegistrationDO;
import com.fairrummy.request.dto.MatchMakingConfigRequestDTO;
import com.fairrummy.request.dto.MatchMakingConfigResponseDTO;
import com.fairrummy.response.dto.TemplateResponseDTO;
import com.fairrummy.service.MatchMakingConfigService;
import com.fairrummy.service.TemplateService;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fairrummy.utility.MatchMakingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchMakingConfigServiceImpl implements MatchMakingConfigService {

    private final MatchMakingConfigDao matchMakingConfigDao;
    private final PCObjectMapper pcObjectMapper;
    private final TemplateService templateService;
    private final MatchMakingServiceRestClient matchMakingServiceRestClient;
    private final MatchMakingUtils matchMakingUtils;

    @Override
    public MatchMakingConfigResponseDTO createMatchMakingConfig(
            MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
            throws MatchMakingConfigBadRequestException {
        long startTime = System.currentTimeMillis();

        validateTemplateIds(
                matchMakingConfigRequestDTO.getTemplateIds());
        log.info("Template Validations completed successfully");

        validateMatchMakingMappingWithTemplate(null, matchMakingConfigRequestDTO);
        log.info("Match Making Config Association with Template Validations completed successfully");
        MatchMakingConfig matchMakingConfig =
                matchMakingConfigDao.createMatchMakingConfig(matchMakingConfigRequestDTO);
        log.info("Match-Making config inserted successfully : {}", matchMakingConfig);

        Set<String> templateIdsForMatchMakingConfig = new HashSet<>(matchMakingConfig.getTemplateIds());
        List<RegistrationDO> registrationDOS =
                matchMakingUtils.getRegistrationDOS(
                        templateIdsForMatchMakingConfig, null, matchMakingConfigRequestDTO);

        if (registrationDOS != null && !registrationDOS.isEmpty()) {
            log.info(
                    "Registering templateIdsForMatchMaking: {} with matchMakingConfigId : {} in Match Maker",
                    templateIdsForMatchMakingConfig,
                    matchMakingConfig.getMatchMakingConfigId());

            ResponseEntity<String> mmsResponse = matchMakingServiceRestClient.bulkUpdate(registrationDOS);
            if (mmsResponse != null && mmsResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
                log.info(
                        "Template Ids: {} is successfully registered with MatchMakingConfigId: {}",
                        templateIdsForMatchMakingConfig,
                        matchMakingConfig.getMatchMakingConfigId());
            } else {
                log.error(
                        "Exception: template Ids: {} couldn't registered with matchmaking configId: {}",
                        templateIdsForMatchMakingConfig,
                        matchMakingConfig.getMatchMakingConfigId());
            }
        }
        try {
            return pcObjectMapper.convertValue(matchMakingConfig, MatchMakingConfigResponseDTO.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while creating the match making config", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    private void validateMatchMakingMappingWithTemplate(
            String matchMakingConfigId, MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
            throws MatchMakingConfigBadRequestException {
        List<MatchMakingConfigResponseDTO> matchMakingConfigResponseDTOS =
                getAllMatchMakingConfig();

        if (!CollectionUtils.isEmpty(matchMakingConfigResponseDTOS)) {
            Set<String> templateIdsOfExistingConfigs =
                    matchMakingConfigResponseDTOS.stream()
                            .filter(
                                    matchMakingConfigResponseDTO ->
                                            !Objects.equals(
                                                    matchMakingConfigResponseDTO.getMatchMakingConfigId(),
                                                    matchMakingConfigId))
                            .map(MatchMakingConfigResponseDTO::getTemplateIds)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet());

            for (String templateId : matchMakingConfigRequestDTO.getTemplateIds()) {
                if (templateIdsOfExistingConfigs.contains(templateId)) {
                    log.error("Math Making Config Already existing for the templateId : {}", templateId);
                    throw new MatchMakingConfigBadRequestException(
                            DUPLICATE_MATCHMAKING_CONFIG_FOR_TEMPLATE.getErrorCode(),
                            DUPLICATE_MATCHMAKING_CONFIG_FOR_TEMPLATE.getHttpStatus(),
                            DUPLICATE_MATCHMAKING_CONFIG_FOR_TEMPLATE.getMessage());
                }
            }
        }
    }

    private void validateTemplateIds(List<String> templateIds)
            throws MatchMakingConfigBadRequestException {
        List<TemplateResponseDTO> resultTemplate = templateService.getTemplateByIds( templateIds);

        if (templateIds.size() != resultTemplate.size()) {
            throw new MatchMakingConfigBadRequestException(
                    INVALID_TEMPLATE_IDS_FOR_MATCH_MAKING_CONFIG.getErrorCode(),
                    INVALID_TEMPLATE_IDS_FOR_MATCH_MAKING_CONFIG.getHttpStatus(),
                    INVALID_TEMPLATE_IDS_FOR_MATCH_MAKING_CONFIG.getMessage());
        }
    }

    @Override
    public MatchMakingConfigResponseDTO updateMatchMakingConfig(
            String matchMakingConfigId, MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
            throws MatchMakingConfigBadRequestException {
        long startTime = System.currentTimeMillis();

        validateTemplateIds(
                matchMakingConfigRequestDTO.getTemplateIds());
        log.info("Template Validations completed successfully");

        validateMatchMakingMappingWithTemplate(matchMakingConfigId, matchMakingConfigRequestDTO);
        log.info("Match Making Config Association with Template Validations completed successfully");

        MatchMakingConfigResponseDTO matchMakingConfigDB = getMatchMakingConfig(matchMakingConfigId);

        matchMakingConfigDao.updateMatchMakingConfig(matchMakingConfigId, matchMakingConfigRequestDTO);
        Set<String> templateIdsForRegister =
                new HashSet<String>(matchMakingConfigRequestDTO.getTemplateIds());

        log.info("Updated MatchMaking Config with id : {} Successfully", matchMakingConfigId);
        // for unregister matchMaking config
        Set<String> templateIdsForUnregister =
                matchMakingConfigDB.getTemplateIds().stream()
                        .filter(templateId -> !templateIdsForRegister.contains(templateId))
                        .collect(Collectors.toSet());
        List<RegistrationDO> registrationDOS =
                matchMakingUtils.getRegistrationDOS(
                        templateIdsForRegister, templateIdsForUnregister, matchMakingConfigRequestDTO);

        if (registrationDOS != null && !registrationDOS.isEmpty()) {
            log.info(
                    "Registering templateIdsForMatchMaking: {}, and Unregistering templateIds : {},  with matchMakingConfigId : {} in Match Maker",
                    templateIdsForRegister,
                    templateIdsForUnregister,
                    matchMakingConfigId);

            ResponseEntity<String> mmsResponse = matchMakingServiceRestClient.bulkUpdate(registrationDOS);
            if (mmsResponse != null && mmsResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
                log.info(
                        "Template Ids: {} is successfully registered and TemplateIds : {} successfully unregistered with MatchMakingConfigId: {}",
                        templateIdsForRegister,
                        templateIdsForUnregister,
                        matchMakingConfigId);
            } else {
                log.error(
                        "Exception: template Ids: {} couldn't registered and templateIds :{} couldn't unregistered with matchmaking configId: {}",
                        templateIdsForRegister,
                        templateIdsForUnregister,
                        matchMakingConfigId);
            }
        }
        try {
            return pcObjectMapper.convertValue(
                    matchMakingConfigRequestDTO, MatchMakingConfigResponseDTO.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while updating the match making config", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    @Override
    public MatchMakingConfigResponseDTO getMatchMakingConfig(String matchMakingConfigId)
            throws MatchMakingConfigBadRequestException {
        long startTime = Instant.now().toEpochMilli();
        MatchMakingConfig matchMakingConfig =
                matchMakingConfigDao.getMatchMakingConfig(matchMakingConfigId);

        if (Objects.isNull(matchMakingConfig)) {
            throw new MatchMakingConfigBadRequestException(
                    MATCH_MAKING_CONFIG_NOT_FOUND.getErrorCode(),
                    MATCH_MAKING_CONFIG_NOT_FOUND.getHttpStatus(),
                    MATCH_MAKING_CONFIG_NOT_FOUND.getMessage());
        }

        try {
            MatchMakingConfigResponseDTO matchMakingConfigResponseDTO =
                    pcObjectMapper.convertValue(matchMakingConfig, MatchMakingConfigResponseDTO.class);
            return matchMakingConfigResponseDTO;
        } catch (RuntimeException e) {
            log.error("Exception occurred while getting the MatchMakingConfig");
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    @Override
    public List<MatchMakingConfigResponseDTO> getAllMatchMakingConfig() throws MatchMakingConfigBadRequestException {
        long startTime = Instant.now().toEpochMilli();
        List<MatchMakingConfig> matchMakingConfigs = matchMakingConfigDao.getAllMatchMakingConfig();

        if (!CollectionUtils.isEmpty(matchMakingConfigs)) {
            try {
                return matchMakingConfigs.stream()
                        .map(
                                matchMakingConfig -> {
                                    MatchMakingConfigResponseDTO makingConfigResponseDTO =
                                            pcObjectMapper.convertValue(
                                                    matchMakingConfig, MatchMakingConfigResponseDTO.class);
                                    return makingConfigResponseDTO;
                                })
                        .collect(Collectors.toList());
            } catch (RuntimeException e) {
                log.error("Exception occurred while getting the MatchMakingConfig", e);
                throw new MatchMakingConfigInternalServerException(
                        UNKNOWN_ERROR.getErrorCode(),
                        UNKNOWN_ERROR.getHttpStatus(),
                        UNKNOWN_ERROR.getMessage());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public MatchMakingConfigResponseDTO getMatchMakingConfigByTemplateId(String templateId) {
        log.info("getMatchMakingConfigByTemplateId request received for templateId : {}", templateId);
        try {
            MatchMakingConfigResponseDTO matchMakingConfigResponseDTO =
                    matchMakingConfigDao.getMatchMakingConfigByTemplateId(templateId);
            return matchMakingConfigResponseDTO;
        } catch (RuntimeException e) {
            log.error("Exception occurred while getMatchMakingConfigByTemplateId", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }
}*/
