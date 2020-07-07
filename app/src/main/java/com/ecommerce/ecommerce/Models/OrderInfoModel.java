package com.ecommerce.ecommerce.Models;

import com.ecommerce.ecommerce.object.Product;

public class OrderInfoModel {
    public OrderInfoModel() {
    }

    private String orderId,date,deliveryDate;
    private Product item;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    public OrderInfoModel(String orderId, String date, String deliveryDate, Product item) {
        this.orderId = orderId;
        this.date = date;
        this.deliveryDate = deliveryDate;
        this.item = item;
    }
}
