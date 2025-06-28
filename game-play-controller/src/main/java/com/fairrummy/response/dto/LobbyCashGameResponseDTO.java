package com.fairrummy.response.dto;

import java.util.List;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LobbyCashGameResponseDTO {
    private List<CashGameTemplate> templates;
    private String cashGameSockerUrl;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class CashGameTemplate {
        protected int id;
        protected int minBuyin;
        protected int maxBuyin;
        protected int status;
        protected int gid;
        protected String name;
        protected int minPlayer;
        protected int maxPlayer;
        private int noOfCards;
        private int gameStartTime;
        private int pointValue;
        private int noOfDeck;
        private int cardsPerPlayer;
        private int playerTurnTime;
        private double serviceFee;
        private int graceTime;
        private int dealsPerGame;
        private int variantType;
        private boolean skillBasedMM;
    }
}
