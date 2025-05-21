package com.fairrummy.service;

import com.fairrummy.request.dto.LobbyValidationRequest;
import com.fairrummy.response.dto.LobbyCashGameResponseDTO;
import com.fairrummy.response.dto.LobbyValidationResponse;

public interface LobbyService {
    public LobbyCashGameResponseDTO getLobbyData(Long userId);

    public LobbyValidationResponse validate(LobbyValidationRequest lobbyValidationRequest);

}
