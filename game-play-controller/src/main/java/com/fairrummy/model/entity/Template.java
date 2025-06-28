package com.fairrummy.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
public class Template
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isSkillBasedMM() {
        return skillBasedMM;
    }
}
