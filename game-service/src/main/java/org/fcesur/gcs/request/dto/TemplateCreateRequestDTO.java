package com.fairrummy.request.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import javax.validation.constraints.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateCreateRequestDTO {

    @Min(1)
    private int minBuyin;

    @Min(1)
    private int maxBuyin;

    @Min(0)
    private int status;

    @Min(1)
    private int gid;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Min(2)
    private int minPlayer;

    @Min(2)
    private int maxPlayer;

    @Min(52)
    private int noOfCards;

    @Min(10000)
    private int gameStartTime;  // in ms

    @Min(1)
    private int pointValue;

    @Min(2)
    private int noOfDeck;

    @Min(13)
    private int cardsPerPlayer;

    @Min(5000)
    private int playerTurnTime;

    @DecimalMin(value = "0.0", inclusive = true)
    private double serviceFee;

    @Min(0)
    private int graceTime;

    @Min(1)
    private int dealsPerGame;

    @Min(0)
    private int variantType;

    private boolean skillBasedMM;

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

    public boolean isSkillBasedMM() {
        return skillBasedMM;
    }
}
