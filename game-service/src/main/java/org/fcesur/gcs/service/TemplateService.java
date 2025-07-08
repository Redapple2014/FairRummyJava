package com.fairrummy.service;

import com.fairrummy.exception.TemplateBadRequestException;
import com.fairrummy.exception.TemplateInternalServerException;
import com.fairrummy.request.dto.TemplateCreateRequestDTO;
import com.fairrummy.response.dto.TemplateResponseDTO;

import java.util.List;

public interface TemplateService {
    TemplateResponseDTO createTemplate(TemplateCreateRequestDTO templateRequestDTO) throws TemplateInternalServerException, TemplateBadRequestException;

    TemplateResponseDTO getTemplate(int templateId) throws TemplateInternalServerException;

    List<TemplateResponseDTO> getAllActiveTemplates() throws TemplateInternalServerException;

    TemplateResponseDTO updateTemplate(int templateId, TemplateCreateRequestDTO templateCreateRequestDTO);
}
