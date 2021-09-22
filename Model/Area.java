package com.bignerdranch.android.tradesexplorer20.Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Area implements Serializable {
    private UUID mId;
    private int xValue;
    private int yValue;
    private String description;
    private boolean town;
    private List<Item> items;
    private boolean active;
    private boolean explored;
    private boolean starred;

    public Area(int x, int y) {
        this.mId = UUID.randomUUID();
        this.xValue = x;
        this.yValue = y;
        this.town = townGen();
        this.items = itemGen();
        this.active = false;
        this.description = "";
        this.explored = false;
        this.starred = false;
    }

    public Area(UUID mId) {
        this.mId = mId;
    }

    /* ==== GETTERS ==== */

    public boolean isTown() {
        return town;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExplored() {
        return explored;
    }

    public boolean isStarred() {
        return starred;
    }

    public int getxValue() {
        return xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public UUID getId() {
        return mId;
    }

    public String getAreaType() {
        if (town == false) {
            return "Wilderness";
        } else {
            return "Town";
        }
    }

    /* ==== SETTERS ==== */

    public void setTown(boolean town) {
        this.town = town;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean townGen() {
        return getRandomBoolean();
    }

    public LinkedList<Item> itemGen() {
        return null;
    }

    public boolean getRandomBoolean() { return Math.random() < 0.5; }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }
}
