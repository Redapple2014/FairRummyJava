package org.fcesur.skillengine.rummy.message;

import org.fcesur.skillengine.rummy.player.SeatPlayerInfo;
import org.fcesur.skillengine.rummy.table.SeatInfo;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<SeatDetails> convertSeatsList(List<SeatInfo> seatInfos, long seatPlayerId) {

        List<SeatDetails> outSeatList = new ArrayList<>();

        for (int i = 0; i < seatInfos.size(); i++) {
            SeatInfo seatInfo = seatInfos.get(i);
            SeatPlayerInfo player = seatInfo.getPlayer();
            if (player == null) {
                continue;
            }
            UserInfo userInfo = new UserInfo(-1, player.getUserId(), player.getUserName(), player.getAvatarId());
            SeatDetails seatDetails = new SeatDetails(-1, seatInfo.getId(), seatInfo.getState(), userInfo, seatInfo.getSeatPlayerBalance());
            outSeatList.add(seatDetails);
        }
        return outSeatList;

    }
}
