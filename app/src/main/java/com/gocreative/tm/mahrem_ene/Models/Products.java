package com.gocreative.tm.mahrem_ene.Models;

public class Products {
    private String name, imageUrl, description;
    private int stock;
    private float price;

    public Products(){}

    public Products(String name, float price, String imageUrl, String description, int stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
