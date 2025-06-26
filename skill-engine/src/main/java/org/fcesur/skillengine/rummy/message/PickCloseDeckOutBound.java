package org.fcesur.skillengine.rummy.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PickCloseDeckOutBound extends Message {
    private long gPlayerId;
    private String pickedCard;

    /**
     * @param serviceType
     * @param msgType
     * @param tableId
     * @param gPlayerId
     * @param pickedCard
     */
    public PickCloseDeckOutBound(long tableId, long gPlayerId, String pickedCard) {
        super(1, MessageConstants.PICK_CLOSED_DECK_OUTBOUND, tableId);
        this.gPlayerId = gPlayerId;
        this.pickedCard = pickedCard;
    }

}
