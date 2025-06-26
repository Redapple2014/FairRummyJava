package org.fcesur.skillengine.rummy.message;

import lombok.NoArgsConstructor;

import static org.fcesur.skillengine.rummy.message.MessageConstants.FMG_RESPONSE;

@NoArgsConstructor
public class FMGResponse extends Message {

    /**
     * Constructor
     *
     * @param tableId Table Id
     */
    public FMGResponse(long tableId) {
        super(1, FMG_RESPONSE, tableId);
    }

}
