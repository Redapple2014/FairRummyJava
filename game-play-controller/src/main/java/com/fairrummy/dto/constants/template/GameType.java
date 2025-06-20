package com.fairrummy.dto.constants.template;

public enum GameType {
    POINTS,
    POOL,
    DEAL;

    public static boolean isPoints(GameType gameType) {
        return gameType == POINTS;
    }

    public static boolean isPool(GameType gameType) {
        return gameType == POOL;
    }

    public static boolean isDeal(GameType gameType) {
        return gameType == DEAL;
    }
}
