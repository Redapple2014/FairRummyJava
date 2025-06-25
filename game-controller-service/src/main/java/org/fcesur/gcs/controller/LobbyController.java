package org.fcesur.gcs.controller;

import org.fcesur.gcs.model.response.ApiResponse;
import org.fcesur.gcs.response.dto.LobbyCashGameResponseDTO;
import org.fcesur.gcs.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/template/contest")
@AllArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    @GetMapping
    public ResponseEntity<ApiResponse<LobbyCashGameResponseDTO>> getAllTemplates(@RequestParam(value = "userId") Long userId) {
        LobbyCashGameResponseDTO lobbyTemplates = lobbyService.getLobbyData(userId);

        return ResponseEntity.status(HttpStatus.OK)
              .body(new ApiResponse<>(HttpStatus.OK.value(), lobbyTemplates));
    }
}
