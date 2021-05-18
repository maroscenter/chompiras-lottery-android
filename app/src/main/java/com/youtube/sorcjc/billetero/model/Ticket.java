package com.youtube.sorcjc.billetero.model;

import com.google.gson.annotations.SerializedName;

public class Ticket {

    private int id;
    private String code;

    @SerializedName("total_points")
    private int totalPoints;

    @SerializedName("commission_earned")
    private float commissionEarned;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("lotteries_list")
    private String lotteries;

    public Ticket(int id, int totalPoints, float commissionEarned, String createdAt, String lotteries) {
        this.id = id;
        this.totalPoints = totalPoints;
        this.commissionEarned = commissionEarned;
        this.createdAt = createdAt;
        this.lotteries = lotteries;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
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

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public float getCommissionEarned() {
        return commissionEarned;
    }

    public void setCommissionEarned(float commissionEarned) {
        this.commissionEarned = commissionEarned;
    }
}
