package org.fcesur.skillengine.rummy.table;

import org.fcesur.skillengine.rummy.player.SeatPlayerInfo;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatInfo {
    private int id;
    private SeatPlayerInfo player;
    private AtomicInteger state; // 0-empty, 1-occupied
    private BigDecimal seatPlayerBalance;


    public SeatInfo(int id) {
        this.id = id;
        this.state = new AtomicInteger(0);
    }

    public SeatInfo(int id, long playerId) {
        this.id = id;
        this.player = new SeatPlayerInfo(playerId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SeatPlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(SeatPlayerInfo player) {
        this.player = player;
    }

    public int getState() {
        return state.intValue();
    }

    public void setState(int state) {
        this.state.set(state);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public BigDecimal getSeatPlayerBalance() {
        return seatPlayerBalance;
    }

    public void removePlayer() {
        this.player = null;
        this.seatPlayerBalance = BigDecimal.ZERO;
        this.state.set(0);
    }

    public void setState(AtomicInteger state) {
        this.state = state;
    }

    public void setSeatPlayerBalance(BigDecimal seatPlayerBalance) {
        this.seatPlayerBalance = seatPlayerBalance;
    }
}
