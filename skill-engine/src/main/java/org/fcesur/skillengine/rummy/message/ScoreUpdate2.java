package org.fcesur.skillengine.rummy.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(builderClassName = "Builder")
public final class ScoreUpdate2 {

    @JsonProperty("table_id")
    private final int tableId;

    @JsonProperty("joker_card_id")
    private final String jokerCardId;

    @JsonProperty("variant")
    private final int variant;

    @JsonProperty("scores")
    private final List<UserScore2> scores;
}