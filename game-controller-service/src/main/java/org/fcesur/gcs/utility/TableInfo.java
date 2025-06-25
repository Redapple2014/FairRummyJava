package org.fcesur.gcs.utility;

import java.io.Serializable;

public class TableInfo implements Serializable {
    private static final long serialVersionUID = 2850061435767767854L;

    private long tableId = 0l;
    private int availableSeats;
    private long gameStartTime = 0;
    private String engineIp;
    private int templateId;
    private int status;
    private int maxPlayer;

    public TableInfo(long tableId) {
        this.tableId = tableId;
    }

    public TableInfo(Long tableId, Integer availableSeats, Integer eligibility, long gameStartTime) {
        this.tableId = tableId;
        this.availableSeats = availableSeats;
        this.gameStartTime = gameStartTime;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    /**
     * @return the gameStartTime
     */
    public long getGameStartTime() {
        return gameStartTime;
    }

    /**
     * @param gameStartTime the gameStartTime to set
     */
    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public String getEngineIp() {
        return engineIp;
    }

    public void setEngineIp(String engineIp) {
        this.engineIp = engineIp;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
              "tableId=" + tableId +
              ", availableSeats=" + availableSeats +
              ", gameStartTime=" + gameStartTime +
              ", engineIp='" + engineIp + '\'' +
              ", templateId=" + templateId +
              ", status=" + status +
              ", maxPlayer=" + maxPlayer +
              '}';
    }
}
