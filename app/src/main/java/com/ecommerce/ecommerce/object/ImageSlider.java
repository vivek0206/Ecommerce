package com.ecommerce.ecommerce.object;

public class ImageSlider {
    String sliderId,imageUrl,categoryName,subCategoryName;

    public ImageSlider(){

    }
    public ImageSlider(String sliderId, String imageUrl, String categoryName, String subCategoryName) {
        this.sliderId = sliderId;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }

    public String getSliderId() {
        return sliderId;
    }

    public void setSliderId(String sliderId) {
        this.sliderId = sliderId;
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

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
