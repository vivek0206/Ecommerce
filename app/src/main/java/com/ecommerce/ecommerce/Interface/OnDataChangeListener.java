package com.ecommerce.ecommerce.Interface;

public interface OnDataChangeListener {
    public void onDataChanged(int size,int price,boolean flag);
    public void onDataRemoveChange();
    public void onCheckOutOfStock(int flag,int price);

}
