package org.fcesur.gcs.model.response;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private Instant timestamp;
    private T payload;

    public ApiResponse(int status, T payload) {
        this.status = status;
        this.payload = payload;
        this.timestamp = Instant.now();
    }
}
