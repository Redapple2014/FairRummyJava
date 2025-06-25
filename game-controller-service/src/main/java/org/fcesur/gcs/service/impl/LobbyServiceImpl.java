package org.fcesur.gcs.service.impl;

import org.fcesur.gcs.mapper.TemplateMapper;
import org.fcesur.gcs.request.dto.LobbyValidationRequest;
import org.fcesur.gcs.response.dto.LobbyCashGameResponseDTO;
import org.fcesur.gcs.response.dto.LobbyValidationResponse;
import org.fcesur.gcs.response.dto.TemplateResponseDTO;
import org.fcesur.gcs.service.LobbyService;
import org.fcesur.gcs.service.TemplateService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private final TemplateService templateService;

    @Override
    public LobbyCashGameResponseDTO getLobbyData(Long userId) {
        List<TemplateResponseDTO> templates = templateService.getAllActiveTemplates();

        if (templates != null && !CollectionUtils.isEmpty(templates)) {
            List<LobbyCashGameResponseDTO.CashGameTemplate> lobbyTemplates = templates.stream()
                  .map(TemplateMapper::mapToLobbyTemplateResponseDTO)
                  .collect(Collectors.toList());

            String socketURL = "";

            return LobbyCashGameResponseDTO.builder().templates(lobbyTemplates).cashGameSockerUrl(socketURL).build();
        }

        return null;
    }

    @Override
    public LobbyValidationResponse validate(LobbyValidationRequest lobbyValidationRequest) {
        //Check balance, valid template, KYC Age Check, Banned State Check needs to be done here
        LobbyValidationResponse response = new LobbyValidationResponse();
        response.setValid(true);

        return response;
    }
}
