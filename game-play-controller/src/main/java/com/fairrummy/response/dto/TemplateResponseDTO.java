package com.fairrummy.response.dto;


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
public class TemplateResponseDTO {
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

    public int getId() {
        return id;
    }

    public int getMinBuyin() {
        return minBuyin;
    }

    public int getMaxBuyin() {
        return maxBuyin;
    }

    public int getStatus() {
        return status;
    }

    public int getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getNoOfCards() {
        return noOfCards;
    }

    public int getGameStartTime() {
        return gameStartTime;
    }

    public int getPointValue() {
        return pointValue;
    }

    public int getNoOfDeck() {
        return noOfDeck;
    }

    public int getCardsPerPlayer() {
        return cardsPerPlayer;
    }

    public int getPlayerTurnTime() {
        return playerTurnTime;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public int getGraceTime() {
        return graceTime;
    }

    public int getDealsPerGame() {
        return dealsPerGame;
    }

    public int getVariantType() {
        return variantType;
    }
}
