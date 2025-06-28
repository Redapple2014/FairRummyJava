package com.fairrummy.controller;

import com.fairrummy.model.response.ApiResponse;
import com.fairrummy.response.dto.LobbyCashGameResponseDTO;
import com.fairrummy.service.LobbyService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/template/contest")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class LobbyController {

    private final LobbyService lobbyService;

    @GetMapping
    public ResponseEntity<ApiResponse<LobbyCashGameResponseDTO>> getAllTemplates(@RequestParam(value= "userId") Long userId)
    {
        LobbyCashGameResponseDTO lobbyTemplates = lobbyService.getLobbyData(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(HttpStatus.OK.value(), lobbyTemplates));
    }
}
