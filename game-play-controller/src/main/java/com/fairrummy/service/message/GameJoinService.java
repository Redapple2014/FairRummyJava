package com.fairrummy.service.message;

import com.fairrummy.request.dto.FMGRequest;
import com.fairrummy.response.dto.FMGResponse;

public interface GameJoinService {
    public FMGResponse joinTable(FMGRequest request);
}
