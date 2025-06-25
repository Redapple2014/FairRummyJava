package org.fcesur.gcs.service;

import org.fcesur.gcs.exception.TemplateBadRequestException;
import org.fcesur.gcs.exception.TemplateInternalServerException;
import org.fcesur.gcs.request.dto.TemplateCreateRequestDTO;
import org.fcesur.gcs.response.dto.TemplateResponseDTO;

import java.util.List;

public interface TemplateService {
    TemplateResponseDTO createTemplate(TemplateCreateRequestDTO templateRequestDTO) throws TemplateInternalServerException, TemplateBadRequestException;

    //void moveTemplateToDraft(String templateId) throws TemplateInternalServerException;

    //void moveTemplateToActive(String templateId) throws TemplateInternalServerException;

    TemplateResponseDTO getTemplate(int templateId) throws TemplateInternalServerException;

    List<TemplateResponseDTO> getAllActiveTemplates() throws TemplateInternalServerException;

    //List<TemplateResponseDTO> getTemplateByIds(List<String> templateIds) throws TemplateInternalServerException;
}
