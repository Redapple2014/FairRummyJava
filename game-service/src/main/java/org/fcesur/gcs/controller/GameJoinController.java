package com.fairrummy.controller;

import com.fairrummy.request.dto.FMGRequest;
import com.fairrummy.response.dto.FMGResponse;
import com.fairrummy.service.message.GameJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gcs/api")
public class GameJoinController {

    @Autowired
    private GameJoinService gameJoinService;

    @PostMapping(value = "/gamejoin",
    produces = {"application/json"},
    consumes = {"application/json"})
    public ResponseEntity<FMGResponse> validate(@RequestBody FMGRequest fmgReq)
    {
        FMGResponse response = gameJoinService.joinTable(fmgReq);
        return ResponseEntity.ok(response);
    }
}
