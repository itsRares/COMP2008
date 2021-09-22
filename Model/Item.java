package com.bignerdranch.android.tradesexplorer20.Model;

import java.io.Serializable;
import java.util.UUID;

public abstract class Item implements Serializable {
    private UUID mId;
    private String type;
    private String title;
    private String description;
    private int value;

    public Item(UUID mId) {
        this.mId = mId;
    }

    public Item(String type, String title, String description, int value) {
        this.type = type;
        this.mId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.value = value;
    }

    /* ==== GETTERS ==== */

    public String getTitle() { return title; }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

    public UUID getId() {
        return mId;
    }

    public String getType() {
        return type;
    }

    /* ==== SETTERS ==== */

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
