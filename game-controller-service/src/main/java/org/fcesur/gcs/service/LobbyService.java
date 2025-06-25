package org.fcesur.gcs.service;

import org.fcesur.gcs.request.dto.LobbyValidationRequest;
import org.fcesur.gcs.response.dto.LobbyCashGameResponseDTO;
import org.fcesur.gcs.response.dto.LobbyValidationResponse;

public interface LobbyService {
    public LobbyCashGameResponseDTO getLobbyData(Long userId);

    public LobbyValidationResponse validate(LobbyValidationRequest lobbyValidationRequest);

}
