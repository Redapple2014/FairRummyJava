package org.fcesur.skillengine.service.message;

import org.fcesur.skillengine.SkillEngine;
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
import org.fcesur.skillengine.rummy.message.PickClosedDeck;
import org.fcesur.skillengine.rummy.message.PickOpenDeck;
import org.fcesur.skillengine.rummy.message.PlayerTableJoin;
import org.fcesur.skillengine.rummy.message.SetHandCards;
import org.fcesur.skillengine.rummy.message.TableCreation;
import org.fcesur.skillengine.rummy.message.TableReconReq;
import org.fcesur.skillengine.rummy.message.handler.DeclareHandler;
import org.fcesur.skillengine.rummy.message.handler.DiscardCardHandler;
import org.fcesur.skillengine.rummy.message.handler.DiscardHandler;
import org.fcesur.skillengine.rummy.message.handler.DropHandler;
import org.fcesur.skillengine.rummy.message.handler.FMGHandler;
import org.fcesur.skillengine.rummy.message.handler.FinishHandler;
import org.fcesur.skillengine.rummy.message.handler.GameHistoryHandler;
import org.fcesur.skillengine.rummy.message.handler.HandCardHandler;
import org.fcesur.skillengine.rummy.message.handler.LeaveBoardHandler;
import org.fcesur.skillengine.rummy.message.handler.PickCloseHandler;
import org.fcesur.skillengine.rummy.message.handler.PickOpenHandler;
import org.fcesur.skillengine.rummy.message.handler.PlayerJoinHandler;
import org.fcesur.skillengine.rummy.message.handler.SetupHandler;
import org.fcesur.skillengine.rummy.message.handler.TableCreationHandler;
import org.fcesur.skillengine.rummy.message.handler.TableReconnectionHandler;
import org.fcesur.skillengine.service.CurrencyService;
import org.fcesur.skillengine.sessions.PlayerSession;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

@Slf4j
public final class ServiceHandler {

    private final ServiceMessageDigester messageDigester;
    private final CurrencyService currencyService;
    private final Jackson jackson;

    /**
     * Constructor
     *
     * @param messageDigester Message digester
     * @param currencyService Currency service
     * @param jackson         Jackson object wrapper
     */
    public ServiceHandler(@NonNull ServiceMessageDigester messageDigester,
                          @NonNull CurrencyService currencyService,
                          @NonNull Jackson jackson) {

        this.messageDigester = messageDigester;
        this.currencyService = currencyService;
        this.jackson = jackson;
    }

    public void parser(@NonNull String gameSrvMsg) {

        // deserialize service message
        final ServiceMessage serviceMessage =
              jackson.readValue(gameSrvMsg, ServiceMessage.class);

        // deserialize player session
        final PlayerSession playerSession =
              messageDigester.deserializeSession(serviceMessage.getPlayerSession());

        // deserialize message
        final Message gamePayloadMessage = messageDigester.deserialize(serviceMessage.getGamePayload());

        // log
        if (log.isDebugEnabled()) {
            log.info("Message: {}", serviceMessage);
        }

        switch (gamePayloadMessage) {
            case PlayerTableJoin playerJoin ->
                  new PlayerJoinHandler(currencyService).handleMessage(playerSession, playerJoin, serviceMessage.getReceiverId());
            case BoardSetup boardSetup ->
                  new SetupHandler().handleMessage(playerSession, boardSetup, serviceMessage.getReceiverId());
            case TableCreation tableCreate ->
                  new TableCreationHandler(currencyService).handleMessage(playerSession, tableCreate, serviceMessage.getReceiverId());
            case PickOpenDeck pickOpen ->
                  new PickOpenHandler().handleMessage(playerSession, pickOpen, serviceMessage.getReceiverId());
            case PickClosedDeck PickClosedDeck ->
                  new PickCloseHandler().handleMessage(playerSession, PickClosedDeck, serviceMessage.getReceiverId());
            case Drop drop -> new DropHandler().handleMessage(playerSession, drop, serviceMessage.getReceiverId());
            case Discard discard ->
                  new DiscardHandler().handleMessage(playerSession, discard, serviceMessage.getReceiverId());
            case Declare declare ->
                  new DeclareHandler().handleMessage(playerSession, declare, serviceMessage.getReceiverId());
            case LeaveBoard leave ->
                  new LeaveBoardHandler().handleMessage(playerSession, leave, serviceMessage.getReceiverId());
            case Finish finish ->
                  new FinishHandler().handleMessage(playerSession, finish, serviceMessage.getReceiverId());
            case DiscardCardReq discardCard ->
                  new DiscardCardHandler().handleMessage(playerSession, discardCard, serviceMessage.getReceiverId());
            case FMGRequest fmgReq ->
                  new FMGHandler(SkillEngine.getInstance().getTableDetailsDAO()).handleMessage(playerSession, fmgReq, serviceMessage.getReceiverId());
            case TableReconReq tableReconReq ->
                  new TableReconnectionHandler().handleMessage(playerSession, tableReconReq, serviceMessage.getReceiverId());
            case SetHandCards handcards ->
                  new HandCardHandler().handleMessage(playerSession, handcards, serviceMessage.getReceiverId());
            case GameHistoryRequest message ->
                  new GameHistoryHandler().handleMessage(playerSession, message, serviceMessage.getReceiverId());
            default -> throw new IllegalArgumentException("Unexpected value: " + gamePayloadMessage);
        }
    }
}
