package com.fairrummy.controller;

import com.fairrummy.request.dto.StatsUpdationRequest;
import com.fairrummy.service.PlayerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/misc")
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
