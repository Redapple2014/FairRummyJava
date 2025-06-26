package org.fcesur.skillengine.rummy.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDeclaringState {
    private long playingPlayer;
    private List<List<String>> groupCards;
    private int points;
    private int status;

}
