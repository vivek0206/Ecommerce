package com.ecommerce.ecommerce.Models;

import android.graphics.drawable.Drawable;

public class AccountModel {
    private Drawable img;
    private String text;
    private int pos;

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public AccountModel(Drawable img, String text, int pos) {
        this.img = img;
        this.text = text;
        this.pos = pos;
    }
}
