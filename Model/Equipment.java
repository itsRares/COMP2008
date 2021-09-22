package com.bignerdranch.android.tradesexplorer20.Model;

import java.util.UUID;

public class Equipment extends Item {
    private Double mass;

    public Equipment(String title, String description, int value, double mass) {
        super("equipment", title, description, value);
        this.mass = mass;
    }

    public Equipment(UUID mId) {
        super(mId);
    }

    /* ==== GETTERS ==== */

    public Double getMass() {
        return mass;
    }

    /* ==== SETTERS ==== */

    public void setMass(Double mass) {
        this.mass = mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return mass != null ? mass.equals(equipment.mass) : equipment.mass == null;
    }
}
