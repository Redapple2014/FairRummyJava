package com.fairrummy.service.impl;

import com.fairrummy.dao.TemplateDao;
import com.fairrummy.exception.TemplateBadRequestException;
import com.fairrummy.exception.TemplateInternalServerException;
import com.fairrummy.model.entity.Template;
import com.fairrummy.request.dto.TemplateCreateRequestDTO;
import com.fairrummy.response.dto.TemplateResponseDTO;
import com.fairrummy.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import static com.fairrummy.mapper.TemplateMapper.mapToResponseDTO;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired private TemplateDao templateDao;

    @Override
    public TemplateResponseDTO createTemplate(TemplateCreateRequestDTO templateRequestDTO) throws TemplateInternalServerException, TemplateBadRequestException {
        Template template = templateDao.createTemplate(templateRequestDTO);
        TemplateResponseDTO responseDTO = mapToResponseDTO(template);
        return responseDTO;
    }

    @Override
    public TemplateResponseDTO getTemplate(int templateId) throws TemplateInternalServerException {
        Template template = templateDao.getTemplate(templateId);
        return mapToResponseDTO(template);
    }

    @Override
    public List<TemplateResponseDTO> getAllActiveTemplates() throws TemplateInternalServerException {
        List<Template> templates = templateDao.searchAllActiveTemplates();

        List<TemplateResponseDTO> responseList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(templates))
        {
            for(Template template: templates)
                responseList.add(mapToResponseDTO(template));
        }

        return responseList;
    }

    @Override
    public TemplateResponseDTO updateTemplate(int templateId, TemplateCreateRequestDTO templateRequestDTO) throws TemplateInternalServerException, TemplateBadRequestException {
        Template template = templateDao.updateTemplate(templateId, templateRequestDTO);
        TemplateResponseDTO responseDTO = mapToResponseDTO(template);
        return responseDTO;
    }
}
