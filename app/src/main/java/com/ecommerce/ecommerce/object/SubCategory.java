package com.ecommerce.ecommerce.object;

public class SubCategory {

    String imageUrl,categoryName,subCategoryName;
    public SubCategory() {

    }

    public SubCategory(String imageUrl, String categoryName, String subCategoryName) {
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
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

    public void setCategoryName(String categotyName) {
        this.categoryName = categotyName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
