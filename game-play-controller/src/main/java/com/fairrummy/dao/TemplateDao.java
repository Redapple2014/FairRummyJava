package com.fairrummy.dao;

import com.fairrummy.exception.TemplateBadRequestException;
import com.fairrummy.exception.TemplateInternalServerException;
import com.fairrummy.mapper.GameTemplateMapper;
import com.fairrummy.model.entity.Template;
import com.fairrummy.request.dto.TemplateCreateRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.fairrummy.mapper.TemplateMapper.mapToTemplate;

@Slf4j
@Component
public class TemplateDao {

    @Autowired
    private GameTemplateMapper gameTemplateMapper;

    public Template createTemplate(TemplateCreateRequestDTO templateCreateRequestDTO)
            throws TemplateInternalServerException, TemplateBadRequestException {

        Template template = mapToTemplate(templateCreateRequestDTO);
        gameTemplateMapper.save(template);

        return template;
    }

    public Template getTemplate(int templateId) {
        return gameTemplateMapper.findById(templateId).orElse(null);
    }

    public List<Template> searchAllActiveTemplates() {
        List<Template> templates = gameTemplateMapper.findAll();
        return templates;
    }

    /*public List<Template> getTemplateByIds(List<String> templateIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("templateId").in(templateIds));

        try {
            return mongoTemplate.find(query, Template.class);
        } catch (RuntimeException e) {
            log.error("Exception occurred while getting Template by game id and template ids", e);
            throw new TemplateInternalServerException(
                    UNKNOWN_ERROR.getErrorCode(), UNKNOWN_ERROR.getHttpStatus(), UNKNOWN_ERROR.getMessage());
        }
    }*/
}
