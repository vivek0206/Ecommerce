package com.ecommerce.ecommerce.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private String imageUrl,categoryName,productName,originalPrice,salePrice,quantity,rating;

    public Product() {
    }

    public Product(String productName) {
        this.productName = productName;
    }

    public Product(String imageUrl, String categoryName, String productName, String originalPrice, String salePrice, String quantity, String rating) {
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.rating = rating;
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

