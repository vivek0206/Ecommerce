package com.ecommerce.ecommerce.Models;

public class UserOrderInfo {

    public UserOrderInfo() {
    }

    private String orderId,orderDate,deliveryDate,totalPrice,status,paymentStatus,paymentTransactionId,shippingCharges,address1,address2,delivery,userId;

    public UserOrderInfo(String orderId, String orderDate, String deliveryDate, String totalPrice, String status, String paymentStatus, String paymentTransactionId, String shippingCharges, String address1, String address2, String delivery, String userId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentTransactionId = paymentTransactionId;
        this.shippingCharges = shippingCharges;
        this.address1 = address1;
        this.address2 = address2;
        this.delivery = delivery;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }


    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
