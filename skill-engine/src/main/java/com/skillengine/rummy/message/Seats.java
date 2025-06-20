package com.skillengine.rummy.message;

import java.util.List;

import static com.skillengine.rummy.message.MessageConstants.SEATS;

public class Seats extends Message {

    private List<SeatDetails> seatInfos;

    public Seats(long tableId, List<SeatDetails> seatInfos) {
        super(1, SEATS, tableId);
        this.seatInfos = seatInfos;

    }

    public List<SeatDetails> getSeatInfos() {
        return seatInfos;
    }

}
