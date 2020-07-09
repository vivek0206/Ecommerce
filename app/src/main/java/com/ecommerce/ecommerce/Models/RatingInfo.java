package com.ecommerce.ecommerce.Models;

public class RatingInfo {
    String ratingName;
    int ratingValue;

    public RatingInfo(String ratingName, int ratingValue) {
        this.ratingName = ratingName;
        this.ratingValue = ratingValue;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public RatingInfo(){

    }

}
