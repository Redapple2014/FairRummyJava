package org.fcesur.gcs.service.message;

import org.fcesur.gcs.request.dto.FMGRequest;
import org.fcesur.gcs.response.dto.FMGResponse;

public interface GameJoinService {
    public FMGResponse joinTable(FMGRequest request);
}
