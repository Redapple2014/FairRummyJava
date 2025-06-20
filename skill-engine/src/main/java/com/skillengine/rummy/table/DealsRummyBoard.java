package com.skillengine.rummy.table;

import com.skillengine.common.GameTemplates;
import com.skillengine.rummy.globals.GameGlobals;
import com.skillengine.rummy.globals.TimeTaskTypes;
import com.skillengine.rummy.globals.VariantTypes;
import com.skillengine.rummy.player.PlayerInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class DealsRummyBoard extends RummyBoard {
    private int currentDealNo;
    private Map<Long, Integer> playerRankMap;
    private AtomicBoolean isAllGamesCompleted = new AtomicBoolean(false);
    private Map<Long, Integer> playerWinningCnt = new ConcurrentHashMap<>();

    public DealsRummyBoard(long tableId, GameTemplates templateDetails) {
        super(tableId, templateDetails);
    }

    @Override
    protected synchronized void checkStatusOnJoin(boolean joinflag) {
        log.info("changeStatusOnJoin : " + getAllplayer() + " TableId : " + getTableId() + " currentDealNo :" + currentDealNo + " gameStartFlag :" + gameStartFlag.get() + " !gameEndFlag  :"
              + !gameEndFlag.get() + "   rummyDeal :" + rummyGame);
        try {
            switch (getAllplayer().size()) {
                case 0:
                    log.info("TableId : " + getTableId() + " changeStatusOnJoin PlayerSize : " + 0);
                    if (gameNo > 0 && !(currTask.getTaskType() == TimeTaskTypes.GAME_END && currTask != null)) {
                        setCompletedStatus();
                    } else {
                        log.info("TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag");
                        gameStartFlag.set(false);
                    }
                    break;
                case 1:
                    if (currentDealNo == 0) {
                        setStatus(GameGlobals.REGISTERING);
                        if (currTask != null)
                            currTask.cancel();
                        if (gameStartFlag.get() && !gameEndFlag.get() && rummyGame != null) {
                            log.info("TableId : " + getTableId() + " Status : " + status + " Deal not yet ended");
                        } else {
                            log.info("TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag");
                            gameStartFlag.set(false);
                        }
                    }
                    break;
                default:
                    if (joinflag) {
                        if (getAllplayer().size() == getGameTemplates().getMinPlayer()) {
                            scheduleStartGame(getGameTemplates().getGameStartTime());
                        }
                    } else if (currentDealNo == 0 && status == GameGlobals.STARTING && getAllplayer().size() < getGameTemplates().getMinPlayer()) {
                        setStatus(GameGlobals.REGISTERING);
                        if (currTask != null)
                            currTask.cancel();
                        if (gameStartFlag.get() && !gameEndFlag.get() && rummyGame != null) {
                            log.info("TableId : " + getTableId() + " Status : " + status + " Deal not yet ended");
                        } else {
                            log.info("TableId : " + getTableId() + " Status : " + status + " Resetting GameStart Flag");
                            gameStartFlag.set(false);
                        }
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean startGame() {
        if (currentDealNo == 0 && getAllplayer().size() < getGameTemplates().getMinPlayer()) {
            log.info("TableId : " + getTableId() + " PlayerCount : " + (getPlayingPlayerSize() + getSeatedPlayerSize()) + " Not starting the game");
            gameStartFlag.set(false);
        } else {
            super.startGame();
            if (gameStartFlag.get()) {
                ++currentDealNo;
            } else if (currentDealNo > 0 && getAllplayer().size() == 1) {

                endGameBeforeDealStart();
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean endGameBeforeDealStart() {
        try {

            List<PlayerInfo> playingPlayer = getPlayingPlayer();
            if (playingPlayer.size() > 1) {
                return false;
            }
            long winner = playingPlayer.get(0).getUserId();
            if (playerRankMap == null) {
                playerRankMap = new HashMap<Long, Integer>();
            }
            playerRankMap.put(winner, 1);
            gameEndFlag.set(true);
            setGameEndStatus();
            gameStartFlag.set(false);
            gameEndFlag.set(false);
            rummyGame = null;
            if (status < GameGlobals.COMPLETED) {
                checkLastGameCriteria();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void checkLastGameCriteria() {
        scheduleStartGame(getGameTemplates().getGameStartTime());
        sendTableSeatMsg(0);
    }

    @Override
    public boolean endGame() {
        boolean status = super.endGame();
        if (currentDealNo == getGameTemplates().getDealsPerGame() && getGameTemplates().getVariantType() == VariantTypes.DEALS_RUMMY) {
            isAllGamesCompleted.set(true);
        }
        long winnerId = getWinner();
        playerWinningCnt.merge(winnerId, 1, Integer::sum);
        if (!isAllGamesCompleted.get()) {
            checkForNextDeal();
            return false;
        }
        // If all the games completed
        int requiredWinningCnt = bestOfNMatch(getGameTemplates().getDealsPerGame());
        for (Long plId : playerWinningCnt.keySet()) {
            Integer cnt = playerWinningCnt.get(plId);
            if (cnt >= requiredWinningCnt) {
                // Deal Winner Will be Announced
            }
        }
        return status;
    }

    public int bestOfNMatch(int n) {
        if (n % 2 == 0) {
            throw new IllegalArgumentException("N must be odd for a 'Best of N' match.");
        }
        return ((n / 2) + 1);
    }
}
