package com.ecommerce.ecommerce.Models;

public class RedeemAward {
    public String orderId,userId,prize,flag;

    public RedeemAward() {
    }

    public RedeemAward(String orderId, String userId, String prize, String flag) {
        this.orderId = orderId;
        this.userId = userId;
        this.prize = prize;
        this.flag = flag;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
