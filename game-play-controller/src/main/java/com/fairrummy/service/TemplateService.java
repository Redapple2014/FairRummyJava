package com.fairrummy.service;

import com.fairrummy.exception.TemplateBadRequestException;
import com.fairrummy.exception.TemplateInternalServerException;
import com.fairrummy.request.dto.TemplateCreateRequestDTO;
import com.fairrummy.response.dto.TemplateResponseDTO;

import java.util.List;

public interface TemplateService {
    TemplateResponseDTO createTemplate(TemplateCreateRequestDTO templateRequestDTO) throws TemplateInternalServerException, TemplateBadRequestException;

    //void moveTemplateToDraft(String templateId) throws TemplateInternalServerException;

    //void moveTemplateToActive(String templateId) throws TemplateInternalServerException;

    TemplateResponseDTO getTemplate(int templateId) throws TemplateInternalServerException;

    List<TemplateResponseDTO> getAllActiveTemplates() throws TemplateInternalServerException;

    //List<TemplateResponseDTO> getTemplateByIds(List<String> templateIds) throws TemplateInternalServerException;
}
