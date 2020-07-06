package com.ecommerce.ecommerce.Models;

public class UserInfo {
    private String userName,userPhone,userPswd,userImageUrl;
    private String deliveryName,deliveryPhone,deliveryFlat,deliveryArea,deliveryLandmark,deliveryState,deliveryCity,deliveryPinCode;

    public UserInfo(String deliveryName, String deliveryPhone, String deliveryFlat, String deliveryArea, String deliveryLandmark, String deliveryState, String deliveryCity, String deliveryPinCode) {
        this.deliveryName = deliveryName;
        this.deliveryPhone = deliveryPhone;
        this.deliveryFlat = deliveryFlat;
        this.deliveryArea = deliveryArea;
        this.deliveryLandmark = deliveryLandmark;
        this.deliveryState = deliveryState;
        this.deliveryCity = deliveryCity;
        this.deliveryPinCode = deliveryPinCode;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryFlat() {
        return deliveryFlat;
    }

    public void setDeliveryFlat(String deliveryFlat) {
        this.deliveryFlat = deliveryFlat;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getDeliveryLandmark() {
        return deliveryLandmark;
    }

    public void setDeliveryLandmark(String deliveryLandmark) {
        this.deliveryLandmark = deliveryLandmark;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryPinCode() {
        return deliveryPinCode;
    }

    public void setDeliveryPinCode(String deliveryPinCode) {
        this.deliveryPinCode = deliveryPinCode;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public UserInfo(String userName, String userPhone, String userPswd, String userImageUrl) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPswd = userPswd;
        this.userImageUrl = userImageUrl;
    }

    public UserInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPswd() {
        return userPswd;
    }

    public void setUserPswd(String userPswd) {
        this.userPswd = userPswd;
    }






}
