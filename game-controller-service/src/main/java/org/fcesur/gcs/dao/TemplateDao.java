package org.fcesur.gcs.dao;

import org.fcesur.gcs.exception.TemplateBadRequestException;
import org.fcesur.gcs.exception.TemplateInternalServerException;
import org.fcesur.gcs.mapper.GameTemplateMapper;
import org.fcesur.gcs.model.entity.Template;
import org.fcesur.gcs.request.dto.TemplateCreateRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.fcesur.gcs.mapper.TemplateMapper.mapToTemplate;

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
