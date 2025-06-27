package org.fcesur.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class UserScore2 {

    /**
     * Player id
     */
    @JsonProperty("player_id")
    private long id;

    @JsonProperty("player_display_name")
    private String displayName;

    /**
     * Player score
     */
    @JsonProperty("score")
    private int score;

    /**
     * Player status
     */
    @JsonProperty("status")
    private int status;

    /**
     * Player transaction amount
     */
    @JsonProperty("amount")
    private double amount;

    /**
     * Player cards
     */
    @JsonProperty("cards")
    private List<List<String>> cards;
}