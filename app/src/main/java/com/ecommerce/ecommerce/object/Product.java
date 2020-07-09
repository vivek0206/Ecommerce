package com.ecommerce.ecommerce.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private String imageUrl,categoryName,productName,originalPrice,salePrice,quantity,rating,productDetail,returnable,payOnDelivery;
    private String subCategoryName,orderStatus;
    private int rate1=0,rate2=0,rate3=0,rate4=0,rate5=0;

    public Product(String imageUrl, String categoryName, String productName, String originalPrice, String salePrice, String quantity, String rating, String productDetail, String returnable, String payOnDelivery, String subCategoryName, String orderStatus, int rate1, int rate2, int rate3, int rate4, int rate5) {
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.rating = rating;
        this.productDetail = productDetail;
        this.returnable = returnable;
        this.payOnDelivery = payOnDelivery;
        this.subCategoryName = subCategoryName;
        this.orderStatus = orderStatus;
        this.rate1 = rate1;
        this.rate2 = rate2;
        this.rate3 = rate3;
        this.rate4 = rate4;
        this.rate5 = rate5;
    }

    public int getRate1() {
        return rate1;
    }

    public void setRate1(int rate1) {
        this.rate1 = rate1;
    }

    public int getRate2() {
        return rate2;
    }

    public void setRate2(int rate2) {
        this.rate2 = rate2;
    }

    public int getRate3() {
        return rate3;
    }

    public void setRate3(int rate3) {
        this.rate3 = rate3;
    }

    public int getRate4() {
        return rate4;
    }

    public void setRate4(int rate4) {
        this.rate4 = rate4;
    }

    public int getRate5() {
        return rate5;
    }

    public void setRate5(int rate5) {
        this.rate5 = rate5;
    }

    public Product(String imageUrl, String categoryName, String productName, String originalPrice, String salePrice, String quantity, String rating, String productDetail, String returnable, String payOnDelivery, String subCategoryName) {
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.rating = rating;
        this.productDetail = productDetail;
        this.returnable = returnable;
        this.payOnDelivery = payOnDelivery;
        this.subCategoryName = subCategoryName;
    }

    public Product(String imageUrl, String categoryName, String productName, String originalPrice, String salePrice, String quantity, String rating, String productDetail, String returnable, String payOnDelivery, String subCategoryName, String orderStatus) {
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.rating = rating;
        this.productDetail = productDetail;
        this.returnable = returnable;
        this.payOnDelivery = payOnDelivery;
        this.subCategoryName = subCategoryName;
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Product() {
    }

    public Product(String productName) {
        this.productName = productName;
    }


    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getReturnable() {
        return returnable;
    }

    public void setReturnable(String returnable) {
        this.returnable = returnable;
    }

    public String getPayOnDelivery() {
        return payOnDelivery;
    }

    public void setPayOnDelivery(String payOnDelivery) {
        this.payOnDelivery = payOnDelivery;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}

