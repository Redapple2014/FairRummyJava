package org.fcesur.gcs.controller;

import org.fcesur.gcs.request.dto.FMGRequest;
import org.fcesur.gcs.response.dto.FMGResponse;
import org.fcesur.gcs.service.message.GameJoinService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gcs/api")
public class GameJoinController {

    private GameJoinService gameJoinService;

    /**
     * Constructor
     *
     * @param gameJoinService Game join service
     */
    public GameJoinController(@NonNull GameJoinService gameJoinService) {
        this.gameJoinService = gameJoinService;
    }

    @PostMapping(value = "/gamejoin",
          produces = {"application/json"},
          consumes = {"application/json"})
    public ResponseEntity<FMGResponse> validate(@RequestBody FMGRequest fmgReq) {
        FMGResponse response = gameJoinService.joinTable(fmgReq);
        return ResponseEntity.ok(response);
    }
}
