package com.ecommerce.ecommerce.Models;

public class ProductVariation {
    private String categoryName,subCategoryName,productName,productVariationName;
    private int quantity,productSalePrice,ProductActualPrice;
    private String imageUrl;

    public ProductVariation(String categoryName, String subCategoryName, String productName, String productVariationName, int quantity, int productSalePrice, int productActualPrice, String imageUrl) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.productName = productName;
        this.productVariationName = productVariationName;
        this.quantity = quantity;
        this.productSalePrice = productSalePrice;
        ProductActualPrice = productActualPrice;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductVariation() {
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVariationName() {
        return productVariationName;
    }

    public void setProductVariationName(String productVariationName) {
        this.productVariationName = productVariationName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductSalePrice() {
        return productSalePrice;
    }

    public void setProductSalePrice(int productSalePrice) {
        this.productSalePrice = productSalePrice;
    }

    public int getProductActualPrice() {
        return ProductActualPrice;
    }

    public void setProductActualPrice(int productActualPrice) {
        ProductActualPrice = productActualPrice;
    }


}
