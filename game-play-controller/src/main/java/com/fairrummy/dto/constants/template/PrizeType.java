package com.fairrummy.dto.constants.template;

public enum PrizeType {
    CASH,
    PRACTICE;

    public static boolean isCash(PrizeType prizeType) {
        return prizeType == CASH;
    }

    public static boolean isPractice(PrizeType prizeType) {
        return prizeType == PRACTICE;
    }
}
