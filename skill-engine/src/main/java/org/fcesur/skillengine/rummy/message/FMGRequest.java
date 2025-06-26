package org.fcesur.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FMGRequest extends Message {

    private int templateId;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param templateId
     */
    public FMGRequest(long tableId, int templateId) {
        super(1, MessageConstants.FMG_REQUEST, tableId);
        this.templateId = templateId;
    }

}
