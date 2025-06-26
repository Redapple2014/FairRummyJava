package org.fcesur.skillengine.rummy.player;

import java.math.BigDecimal;

public class PlayerInfo {
    private final long userId;
    private final String userName;
    private final int avatarId;
    private BigDecimal depositBalance;
    private BigDecimal withdrawable;
    private BigDecimal nonWithdrawable;

    public PlayerInfo(long userId, String userName, int avatarId, BigDecimal depositBalance, BigDecimal withdrawable,
                      BigDecimal nonWithdrawable) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.avatarId = avatarId;
        this.depositBalance = depositBalance;
        this.withdrawable = withdrawable;
        this.nonWithdrawable = nonWithdrawable;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public BigDecimal getDepositBalance() {
        return depositBalance;
    }

    public BigDecimal getWithdrawable() {
        return withdrawable;
    }

    public BigDecimal getNonWithdrawable() {
        return nonWithdrawable;
    }


}
