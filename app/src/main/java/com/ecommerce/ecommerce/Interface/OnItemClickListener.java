package com.ecommerce.ecommerce.Interface;

import com.ecommerce.ecommerce.Models.ProductVariation;
import com.ecommerce.ecommerce.object.Product;

public interface OnItemClickListener {

    void onItemClick(Product model,int type);
    void onItemClick();
    void onItemClick(ProductVariation model);

}
