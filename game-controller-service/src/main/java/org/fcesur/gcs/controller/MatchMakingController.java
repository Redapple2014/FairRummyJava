package org.fcesur.gcs.controller;

/*import com.fairrummy.exception.MatchMakingConfigBadRequestException;
import com.fairrummy.model.response.ApiResponse;
import com.fairrummy.request.dto.MatchMakingConfigRequestDTO;
import com.fairrummy.request.dto.MatchMakingConfigResponseDTO;
import com.fairrummy.service.MatchMakingConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/match-making/config")
@RequiredArgsConstructor
public class MatchMakingController {

    private final MatchMakingConfigService matchMakingConfigService;

    @PostMapping
    public ResponseEntity<ApiResponse<MatchMakingConfigResponseDTO>> createMatchMakingConfig(
            @Valid @RequestBody MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
            throws MatchMakingConfigBadRequestException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                HttpStatus.ACCEPTED.value(),
                                matchMakingConfigService.createMatchMakingConfig(matchMakingConfigRequestDTO)));
    }

    @PutMapping("/{matchMakingConfigId}")
    public ResponseEntity<ApiResponse<MatchMakingConfigResponseDTO>> updateMatchMakingConfig(
            @PathVariable String matchMakingConfigId,
            @Valid @RequestBody MatchMakingConfigRequestDTO matchMakingConfigRequestDTO)
            throws MatchMakingConfigBadRequestException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                HttpStatus.ACCEPTED.value(),
                                matchMakingConfigService.updateMatchMakingConfig(
                                        matchMakingConfigId, matchMakingConfigRequestDTO)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MatchMakingConfigResponseDTO>> getMatchMakingConfig(
            @NotBlank @RequestParam String matchMakingConfigId)
            throws MatchMakingConfigBadRequestException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                HttpStatus.ACCEPTED.value(),
                                matchMakingConfigService.getMatchMakingConfig(matchMakingConfigId)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MatchMakingConfigResponseDTO>>> getAllMatchMakingConfig(
            @NotBlank @RequestParam String gameId) throws MatchMakingConfigBadRequestException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                HttpStatus.ACCEPTED.value(),
                                matchMakingConfigService.getAllMatchMakingConfig()));
    }

    @GetMapping("/getByTemplate")
    public ResponseEntity<ApiResponse<MatchMakingConfigResponseDTO>> getMatchMakingConfigByTemplateId(
            @NotBlank @RequestParam String templateId) throws MatchMakingConfigBadRequestException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                HttpStatus.ACCEPTED.value(),
                                matchMakingConfigService.getMatchMakingConfigByTemplateId(templateId)));
    }
}*/
