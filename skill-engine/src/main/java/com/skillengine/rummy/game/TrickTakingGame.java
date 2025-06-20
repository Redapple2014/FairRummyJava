package com.skillengine.rummy.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TrickTakingGame extends GameTimer {

    private AtomicInteger gameState = new AtomicInteger(0);
    private long gameId = 0;
    private ArrayList<Long> orderedPlayerIds = new ArrayList<Long>();
    private Map<Long, Integer> knockedOutPlayer = new ConcurrentHashMap<Long, Integer>();
    private long curPlayingPlayer;

    private AtomicInteger currentPlayerIndex = new AtomicInteger(0);

    public Long switchtTurn() {
        if (knockedOutPlayer.size() < orderedPlayerIds.size()) {
            int cnt = 0;
            long nextPlayer = moveToNextPlayer(curPlayingPlayer);
            while (knockedOutPlayer.containsKey(nextPlayer) && cnt++ < orderedPlayerIds.size()) {
                nextPlayer = moveToNextPlayer(nextPlayer);
            }
            curPlayingPlayer = nextPlayer;
        }
        return curPlayingPlayer;
    }

    private long moveToNextPlayer(long playerId) {
        long nextplayer = 0l;
        int playerIndex = 0;
        if (currentPlayerIndex.intValue() == orderedPlayerIds.size() - 1) {
            currentPlayerIndex.set(0);
            playerIndex = currentPlayerIndex.get();
        } else {
            playerIndex = currentPlayerIndex.incrementAndGet();
        }
        nextplayer = orderedPlayerIds.get(playerIndex);
        return nextplayer;
    }

    public Long getlastPlayer() {

        long dealerid = orderedPlayerIds.get(orderedPlayerIds.size() - 1);
        return dealerid;
    }

    public Long getlastPlayingPlayer() {

        long dealerid = orderedPlayerIds.get(orderedPlayerIds.size() - 1);
        int i = 1;
        while (knockedOutPlayer.containsKey(dealerid)) {
            dealerid = orderedPlayerIds.get(orderedPlayerIds.size() - (1 + i));
            i++;
        }
        return dealerid;
    }

    public void setKnockedPlayer(long mPlayerId, Integer score) {
        knockedOutPlayer.put(mPlayerId, score);
    }

    public int getKnockedPlayerSize() {
        return knockedOutPlayer.size();
    }

    public int getPlayerSize() {
        return orderedPlayerIds.size();
    }

    public long whoseMove() {
        return curPlayingPlayer;
    }

    public abstract void scheduleMoveTimeOut(long time);

    public abstract void moveTimeOut();

    public void addOrderedPlayerIds(Long playerId) {
        orderedPlayerIds.add(playerId);
    }

    public Integer getGameState() {
        return gameState.get();
    }

    public void setGameState(Integer gameState) {
        // this.gameState = gameState;
        this.gameState.set(gameState);
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public void setCurrentPlayer(long mpid) {
        if (mpid > 0 && orderedPlayerIds.contains(mpid)) {
            curPlayingPlayer = mpid;
        } else {
            curPlayingPlayer = orderedPlayerIds.get(0);
        }
    }

    public void setFirstAvlCurrentPlayer() {
        curPlayingPlayer = orderedPlayerIds.get(0);
        while (knockedOutPlayer.containsKey(curPlayingPlayer)) {
            curPlayingPlayer = moveToNextPlayer(curPlayingPlayer);
        }
    }

    public boolean checkKnockedOutPlayer(long mpid) {
        return knockedOutPlayer.containsKey(mpid);
    }

    public ArrayList<Long> getOrderedPlayerIds() {
        return orderedPlayerIds;
    }

    public Map<Long, Integer> getKnockedOutPlayer() {
        return knockedOutPlayer;
    }

    public int getPlayerScore(long mpid) {
        int score = -1;
        if (knockedOutPlayer.containsKey(mpid)) {
            score = knockedOutPlayer.get(mpid);
        }
        return score;
    }

    public ArrayList<Long> getCurrentlyPlayingPlayer() {
        ArrayList<Long> currentPlayers = new ArrayList<Long>(4);
        for (Long playerId : orderedPlayerIds) {
            if (!knockedOutPlayer.containsKey(playerId)) {
                currentPlayers.add(playerId);
            }
        }
        return currentPlayers;
    }

}
