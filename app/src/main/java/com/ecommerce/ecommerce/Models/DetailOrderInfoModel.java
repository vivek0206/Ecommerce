package com.ecommerce.ecommerce.Models;

import com.ecommerce.ecommerce.object.Product;

public class DetailOrderInfoModel {

    public DetailOrderInfoModel() {
    }
    private String orderId;
    private Product item;

    public DetailOrderInfoModel(String orderId, Product item) {
        this.orderId = orderId;
        this.item = item;
    }
}
