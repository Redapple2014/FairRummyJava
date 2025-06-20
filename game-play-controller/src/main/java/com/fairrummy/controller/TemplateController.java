package com.fairrummy.controller;

import com.fairrummy.exception.TemplateBadRequestException;
import com.fairrummy.model.response.ApiResponse;
import com.fairrummy.request.dto.TemplateCreateRequestDTO;
import com.fairrummy.response.dto.TemplateResponseDTO;
import com.fairrummy.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
//import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/template")
//@Slf4j
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateResponseDTO>> createTemplate(
          @Valid @RequestBody TemplateCreateRequestDTO templateCreateRequestDTO)
          throws TemplateBadRequestException {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
              .body(
                    new ApiResponse<>(
                          HttpStatus.ACCEPTED.value(),
                          templateService.createTemplate(templateCreateRequestDTO)));
    }

    //    @PutMapping("/disable")
    //    public ResponseEntity<ApiResponse> moveTemplateToDraft( @NotBlank @RequestParam String templateId)
    //            throws TemplateBadRequestException {
    //
    //        templateService.moveTemplateToDraft(templateId);
    //        return ResponseEntity.status(HttpStatus.ACCEPTED)
    //                .body(new ApiResponse<>(HttpStatus.ACCEPTED.value(), null));
    //    }
    //
    //    @PutMapping("/enable")
    //    public ResponseEntity<ApiResponse> moveTemplateToActive( @NotBlank @RequestParam String templateId)
    //            throws TemplateBadRequestException {
    //
    //        templateService.moveTemplateToActive(templateId);
    //        return ResponseEntity.status(HttpStatus.ACCEPTED)
    //                .body(new ApiResponse<>(HttpStatus.ACCEPTED.value(), null));
    //    }

    @GetMapping
    public ResponseEntity<ApiResponse<TemplateResponseDTO>> getTemplate(
          @NotBlank @RequestParam int templateId) throws TemplateBadRequestException {

        System.out.println("Template details for the Template Id " + templateId);
        TemplateResponseDTO templateResponseDTO = templateService.getTemplate(templateId);

        return ResponseEntity.status(HttpStatus.OK)
              .body(new ApiResponse<>(HttpStatus.OK.value(), templateResponseDTO));
    }
}
