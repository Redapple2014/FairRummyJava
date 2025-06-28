package com.fairrummy.controller;

import com.fairrummy.request.dto.StatsUpdationRequest;
import com.fairrummy.service.PlayerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/misc")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private PlayerStatsService playerStatsService;

    @PostMapping("/updateSkill")
    public ResponseEntity<Boolean> updatePlayerSkill(
            @Valid @RequestBody StatsUpdationRequest updationRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(playerStatsService.updateSkill(updationRequest.getPlayerId(), updationRequest.getSkill()));
    }
}
