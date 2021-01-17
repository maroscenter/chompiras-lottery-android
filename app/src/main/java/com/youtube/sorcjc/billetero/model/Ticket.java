package com.youtube.sorcjc.billetero.model;

public class Ticket {

    private int id;
    private String createdAt;
    private String lotteries;

    public Ticket(int ticketNumber, String createdAt, String lotteries) {
        this.id = ticketNumber;
        this.setCreatedAt(createdAt);
        this.setLotteries(lotteries);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLotteries() {
        return lotteries;
    }

    public void setLotteries(String lotteries) {
        this.lotteries = lotteries;
    }
}
