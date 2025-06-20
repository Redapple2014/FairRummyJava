package com.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.skillengine.rummy.message.MessageConstants.DROP;

@Getter
@NoArgsConstructor
public class Drop extends Message {

    public static final int SERVICE_TYPE = 1;

    /**
     * Constructor
     *
     * @param tableId Table Id
     */
    public Drop(long tableId) {
        super(SERVICE_TYPE, DROP, tableId);
    }
}
