package org.fcesur.gcs.utility;

/*import com.fairrummy.constants.Constants;
import com.fairrummy.dao.TemplateDao;
import com.fairrummy.model.entity.*;
import com.fairrummy.request.dto.MatchMakingConfigRequestDTO;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MatchMakingUtils {

    @Autowired private TemplateDao gameTemplateDao;

    private static final String KAFKA_TOPIC_FOR_MATCHMAKER = "pcMatchMaker";
    private static final String SHARDING_ID = "shardingId";
    private static final String SHARDING_ID_DATATYPE = "string";

    public RegistrationDO getRegistrationDO(Template template) {
        RegistrationDO registrationDO = new RegistrationDO();
        registrationDO.setPollIntervalMs(
                 Constants.POLL_INTERVAL_IN_MS_DEFAULT);
        registrationDO.setCommunication(Constants.Communication.PUSH.getType());
        registrationDO.setTenantId("tenant");
        registrationDO.setInterval(100);
        registrationDO.setShardingId(template.getTemplateId());
        GroupDO grouping = new GroupDO();
        grouping.setDistribution(Constants.Distribution.MAX.getType());
        grouping.setMaxPlayers(template.getMaxPlayers());
        grouping.setMinPlayers(template.getMinPlayers());
        registrationDO.setGrouping(grouping);
        List<AttributeDO> staticAttributes = new ArrayList<>();
        AttributeDO attributeDO = new AttributeDO();
        attributeDO.setName(SHARDING_ID);
        attributeDO.setType(SHARDING_ID_DATATYPE);
        attributeDO.setShardKey(true);
        staticAttributes.add(attributeDO);
        registrationDO.setStaticAttributes(staticAttributes);

        return registrationDO;
    }

    public List<RegistrationDO> getRegistrationDOS(
            Set<String> templateIdsForRegister,
            Set<String> templateIdsForUnRegister,
            MatchMakingConfigRequestDTO matchMakingConfigRequestDTO) {

        List<RegistrationDO> registrationDOList = new ArrayList<>();

        if (!Objects.isNull(templateIdsForRegister) && !templateIdsForRegister.isEmpty()) {
            List<String> templateIdForRegisterList = new ArrayList<>(templateIdsForRegister);

            List<Template> templatesForRegister =
                    gameTemplateDao.getTemplateByIds(
                            templateIdForRegisterList);
            List<RegistrationDO> registrationDOListRegister =
                    templatesForRegister.stream()
                            .map(template -> getRegistrationDO(matchMakingConfigRequestDTO, template))
                            .collect(Collectors.toList());

            registrationDOList.addAll(registrationDOListRegister);
        }

        if (!Objects.isNull(templateIdsForUnRegister) && !templateIdsForUnRegister.isEmpty()) {
            List<String> templateIdForUnRegisterList = new ArrayList<>(templateIdsForUnRegister);

            List<Template> templatesForUnRegister =
                    gameTemplateDao.getTemplateByIds(templateIdForUnRegisterList);

            List<RegistrationDO> registrationDOListUnregister =
                    templatesForUnRegister.stream()
                            .map(this::getRegistrationDO)
                            .collect(Collectors.toList());

            registrationDOList.addAll(registrationDOListUnregister);
        }
        return registrationDOList;
    }

    public RegistrationDO getRegistrationDO(
            MatchMakingConfigRequestDTO matchMakingConfigRequestDTO, Template template) {
        RegistrationDO registrationDO = getRegistrationDO(template);

        if (matchMakingConfigRequestDTO.getSkillVsWaitTime() != null) {
            ExpansionDO expansionDO = new ExpansionDO();
            expansionDO.setTarget(Constants.MatchMakingAttribute.SKILL.getType());
            matchMakingConfigRequestDTO
                    .getSkillVsWaitTime()
                    .keySet()
                    .forEach(
                            skill -> {
                                ExpansionStep step =
                                        new ExpansionStep(
                                                matchMakingConfigRequestDTO.getSkillVsWaitTime().get(skill) / 1000,
                                                Long.valueOf(skill));
                                expansionDO.addStep(step);
                            });
            registrationDO.addExpansion(expansionDO);
        }
        return registrationDO;
    }
}*/
