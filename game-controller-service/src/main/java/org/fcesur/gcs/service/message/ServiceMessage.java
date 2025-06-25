package org.fcesur.gcs.service.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMessage {
    private String gamePayload;
    private String playerSession;
    private long receiverId;
}
