package com.example.generalstore.Modules;

import android.content.ClipData;

public class ItemsAvailable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String name;
    String quantity;
    String Description;
    String price;

    public ItemsAvailable(){}

    public ItemsAvailable(String name) {
        this.name = name;
    }

    public ItemsAvailable(String name, String quantity, String description, String price, String category, String image, String time) {
        this.name = name;
        this.quantity = quantity;
        this.Description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.time = time;
    }

    String category;
    String image;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void getName(String key) {
    }
}
