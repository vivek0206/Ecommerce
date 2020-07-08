package com.ecommerce.ecommerce.Models;

public class SearchModel {
    private String searchName,category,subCategory,productName;
    private int type;

    //1-category,2-subCategory,3-product


    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SearchModel(String searchName, String category, String subCategory, String productName, int type) {
        this.searchName = searchName;
        this.category = category;
        this.subCategory = subCategory;
        this.productName = productName;
        this.type = type;
    }

    public SearchModel() {
    }

}
