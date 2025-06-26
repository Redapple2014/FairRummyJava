package org.fcesur.skillengine.rummy.message;

/**
 * Message constant(s)
 */
public final class MessageConstants {

    public static final String BOARD_INFO = "boardInfo";
    public static final String SEAT_INFO = "seatinfo";
    public static final String SEATS = "seats";
    public static final String USER_INFO = "userinfo";
    public static final String BOARD_SETUP = "boardSetup";
    public static final String PLAYER_TABLE_JOIN = "playerTableJoin";
    public static final String TABLE_CREATE = "tableCreation";
    public static final String GAME_STATUS = "gameStatus";
    public static final String GAME_SETUP = "gameSetup";
    public static final String GAME_TOSS = "gameToss";
    public static final String TOSS_INFO = "tossInfo";
    public static final String PLAYER_SEQ = "playerSequence";
    public static final String PLAYER_TURN = "playerTurnIntimation";
    public static final String PLAYER_TIME_OUT_DISCARD = "playerTimeOutDiscard";
    public static final String BOOT_OUT = "bootOut";
    public static final String PLAYER_TURN_OUT = "playerTurnOut";
    public static final String RESHUFFLE = "reshuffle";
    public static final String PICK_CLOSE_DECK = "pickCloseDeck";
    public static final String PICK_OPEN_DECK = "pickOpenDeck";
    public static final String PICK_OPEN_DECK_OUTBOUND = "pickOpenDeckOutBound";
    public static final String PICK_CLOSED_DECK_OUTBOUND = "pickClosedDeckOutBound";
    public static final String DISCARD = "discard";
    public static final String DISCARD_OUTBOUND = "discardOutBound";
    public static final String DECLARE_EVENT = "declareEvent";
    public static final String DECLARE_TIME_OUT = "declareTimeOut";
    public static final String FINISH = "finish";
    public static final String FINISH_PLAYER_TIMEOUT = "finishPlayerTimeOut";
    public static final String DEAL_WINNER = "dealWinner";
    public static final String FINISH_INIT = "finishInit";
    public static final String DECLARE_DETAILS = "declaringDetails";
    public static final String SCORE_UPDATE = "scoreUpdate";
    public static final String RESET_CARDS = "resetCards";
    public static final String DROP = "drop";
    public static final String DROP_RESPONSE = "dropResponse";
    public static final String WRONG_FINISH = "wrongFinish";
    public static final String DECLARE = "declare";
    public static final String DECLARE_SERVER = "declareServer";
    public static final String LEAVE_BOARD = "leaveBoard";
    public static final String REQUEST_DISCARD_CARDS = "reqDiscardCards";
    public static final String DISCARDED_CARDS = "discardedCards";
    public static final String FMG_REQUEST = "FMGReq";
    public static final String FMG_RESPONSE = "FMGResponse";
    public static final String UPDATE_SEAT_BALANCE = "updateSeatBalance";
    public static final String TABLE_RECON_REQ = "tableReconReq";
    public static final String SET_HAND_CARDS = "setHandCards";

    public static final String GAME_HISTORY_REQUEST = "gameHistoryReq";
    public static final String GAME_HISTORY_RESPONSE = "gameHistoryResponse";

    /**
     * Private constructor
     */
    private MessageConstants() {
        // do nothing
    }
}
