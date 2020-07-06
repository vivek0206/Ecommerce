package com.ecommerce.ecommerce.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    List<String>imageUrl;
    String title;
    String salePrice;
    String originalPrice;
    String quantity;
    String availability;
    String description;
    List<String>rating;

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRating() {
        return rating;
    }

    public void setRating(List<String> rating) {
        this.rating = rating;
    }
    public Product(String title){
        this.title=title;
    }

}

