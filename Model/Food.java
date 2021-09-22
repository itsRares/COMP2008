package com.bignerdranch.android.tradesexplorer20.Model;

import java.util.UUID;

public class Food extends Item {
    private double health;

    public Food(UUID mId) {
        super(mId);
    }

    public Food(String title, String description, int value, double health) {
        super("food", title, description, value);
        this.health = health;
    }

    /* ==== GETTERS ==== */

    public double getHealth() {
        return health;
    }

    /* ==== SETTERS ==== */

    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Double.compare(food.health, health) == 0;
    }
}
