package com.fairrummy.dao;

/*import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static com.fairrummy.constants.ErrorCodes.DUPLICATE_MATCHMAKING_CONFIG;
import static com.fairrummy.constants.ErrorCodes.UNKNOWN_ERROR;


import com.fairrummy.exception.MatchMakingConfigBadRequestException;
import com.fairrummy.exception.MatchMakingConfigInternalServerException;
import com.fairrummy.mapper.PCObjectMapper;
import com.fairrummy.model.entity.MatchMakingConfig;
import com.fairrummy.request.dto.MatchMakingConfigRequestDTO;
import com.fairrummy.request.dto.MatchMakingConfigResponseDTO;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchMakingConfigDao {

    private final MongoTemplate mongoTemplate;
    private final PCObjectMapper mapper;

    public MatchMakingConfig createMatchMakingConfig(
            MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
            throws MatchMakingConfigBadRequestException {
        MatchMakingConfig matchMakingConfig =
                mapper.convertValue(matchMakingConfigRequestDTO, MatchMakingConfig.class);

        matchMakingConfig.setCreatedAt(Instant.now().toEpochMilli());
        matchMakingConfig.setUpdatedAt(Instant.now().toEpochMilli());
        matchMakingConfig.setIsActive(true);

        try {
            return mongoTemplate.insert(matchMakingConfig);
        } catch (DuplicateKeyException e) {
            log.error("Exception Occurred as MatchMaking config already exists", e);
            throw new MatchMakingConfigBadRequestException(
                    DUPLICATE_MATCHMAKING_CONFIG.getErrorCode(),
                    DUPLICATE_MATCHMAKING_CONFIG.getHttpStatus(),
                    DUPLICATE_MATCHMAKING_CONFIG.getMessage());
        } catch (Exception e) {
            log.error("Exception occurred while inserting match making config to Mongo DB", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    public MatchMakingConfig getMatchMakingConfig(String matchMakingConfigId) {
        Query query = new Query(Criteria.where("matchMakingConfigId").is(matchMakingConfigId));
        query.addCriteria(Criteria.where("isActive").is(true));

        try {
            return mongoTemplate.findOne(query, MatchMakingConfig.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while getting MatchMaking Config", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    public void updateMatchMakingConfig(
            String matchMakingConfigId, MatchMakingConfigRequestDTO matchMakingConfigRequestDTO) {

        MatchMakingConfig matchMakingConfig =
                mapper.convertValue(matchMakingConfigRequestDTO, MatchMakingConfig.class);
        Update updateQuery = new Update();
        updateQuery.set("skillVsWaitTime", matchMakingConfig.getSkillVsWaitTime());
        updateQuery.set("templateIds", matchMakingConfig.getTemplateIds());
        updateQuery.set("isActive", matchMakingConfig.getIsActive());
        updateQuery.set("searchTimeout", matchMakingConfig.getSearchTimeout());
        updateQuery.set("gameFormat", matchMakingConfig.getGameFormat());
        updateQuery.set("updatedAt", new Date(System.currentTimeMillis()));

        try {
            mongoTemplate.findAndModify(
                    query(
                            where("matchMakingConfigId")
                                    .is(matchMakingConfigId)
                                    .and("isActive")
                                    .is(true)),
                    updateQuery,
                    MatchMakingConfig.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while updating MatchMaking Config", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    public List<MatchMakingConfig> getAllMatchMakingConfig() {
        Query query = new Query(Criteria.where("isActive").is(true));
        try {
            return mongoTemplate.find(query, MatchMakingConfig.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while getting all MatchMaking Config", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }

    public MatchMakingConfigResponseDTO getMatchMakingConfigByTemplateId(String templateId) {
        Query query = new Query(Criteria.where("isActive").is(true));
        query.addCriteria(Criteria.where("templateIds").in(templateId));
        try {
            MatchMakingConfig matchMakingConfig = mongoTemplate.findOne(query, MatchMakingConfig.class);
            return mapper.convertValue(matchMakingConfig, MatchMakingConfigResponseDTO.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while getting all MatchMaking Config", e);
            throw new MatchMakingConfigInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }
}*/
