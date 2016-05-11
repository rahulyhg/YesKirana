package com.sked.core.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Product {
    private String itemId;
    private String itemName;
    private String weight;
    private String prize;
    private String newPrize;
    private String itemImage;
    private int number;
    private String cart_id;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getNewPrize() {
        return newPrize;
    }

    public void setNewPrize(String newPrize) {
        this.newPrize = newPrize;
    }


    public Product getProductList(JSONObject jsonObject) {
        try {
            this.itemId = jsonObject.getString("product_id");
            this.itemName = jsonObject.getString("product_name");
            this.itemImage = jsonObject.getString("product_image");
            this.prize = jsonObject.getString("product_price");
            this.number = jsonObject.getInt("product_qnty");
            this.newPrize=jsonObject.getString("special_price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
