package org.fcesur.skillengine.service.message;

import org.fcesur.skillengine.message.parsers.Jackson;
import org.fcesur.skillengine.rummy.message.BoardSetup;
import org.fcesur.skillengine.rummy.message.Declare;
import org.fcesur.skillengine.rummy.message.Discard;
import org.fcesur.skillengine.rummy.message.DiscardCardReq;
import org.fcesur.skillengine.rummy.message.Drop;
import org.fcesur.skillengine.rummy.message.FMGRequest;
import org.fcesur.skillengine.rummy.message.Finish;
import org.fcesur.skillengine.rummy.message.GameHistoryRequest;
import org.fcesur.skillengine.rummy.message.LeaveBoard;
import org.fcesur.skillengine.rummy.message.Message;
import org.fcesur.skillengine.rummy.message.MessageConstants;
import org.fcesur.skillengine.rummy.message.PickClosedDeck;
import org.fcesur.skillengine.rummy.message.PickOpenDeck;
import org.fcesur.skillengine.rummy.message.PlayerTableJoin;
import org.fcesur.skillengine.rummy.message.SetHandCards;
import org.fcesur.skillengine.rummy.message.TableCreation;
import org.fcesur.skillengine.rummy.message.TableReconReq;
import org.fcesur.skillengine.sessions.PlayerSession;
import org.jspecify.annotations.Nullable;

public class ServiceMessageDigester {
    private Jackson jackson;

    public ServiceMessageDigester(Jackson jackson) {
        this.jackson = jackson;
    }

    public PlayerSession deserializeSession(String userSession) {
        return jackson.readValue(userSession, PlayerSession.class);
    }

    public @Nullable Message deserialize(String payload) {

        final String messageType = jackson.getMsgType(payload);

        return switch (messageType) {
            case MessageConstants.PLAYER_TABLE_JOIN -> jackson.readValue(payload, PlayerTableJoin.class);
            case MessageConstants.BOARD_SETUP -> jackson.readValue(payload, BoardSetup.class);
            case MessageConstants.TABLE_CREATE -> jackson.readValue(payload, TableCreation.class);
            case MessageConstants.PICK_CLOSE_DECK -> jackson.readValue(payload, PickClosedDeck.class);
            case MessageConstants.PICK_OPEN_DECK -> jackson.readValue(payload, PickOpenDeck.class);
            case MessageConstants.DROP -> jackson.readValue(payload, Drop.class);
            case MessageConstants.FINISH -> jackson.readValue(payload, Finish.class);
            case MessageConstants.DISCARD -> jackson.readValue(payload, Discard.class);
            case MessageConstants.DECLARE -> jackson.readValue(payload, Declare.class);
            case MessageConstants.LEAVE_BOARD -> jackson.readValue(payload, LeaveBoard.class);
            case MessageConstants.REQUEST_DISCARD_CARDS -> jackson.readValue(payload, DiscardCardReq.class);
            case MessageConstants.FMG_REQUEST -> jackson.readValue(payload, FMGRequest.class);
            case MessageConstants.TABLE_RECON_REQ -> jackson.readValue(payload, TableReconReq.class);
            case MessageConstants.SET_HAND_CARDS -> jackson.readValue(payload, SetHandCards.class);
            case MessageConstants.GAME_HISTORY_REQUEST -> jackson.readValue(payload, GameHistoryRequest.class);
            default -> null;
        };
    }

}
