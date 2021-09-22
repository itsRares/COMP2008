package com.bignerdranch.android.tradesexplorer20.Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Player implements Serializable {
    private UUID mId;
    private int rowLocation;
    private int colLocation;
    private int cash;
    private double health;
    private double equipmentMass;
    private List<Equipment> equipment;
    private Area currentArea;

    public Player(UUID mId) {
        this.mId = mId;
        this.equipment = new LinkedList<Equipment>();
    }

    public Player(Area inArea) {
        this.mId = UUID.randomUUID();
        this.rowLocation = 0;
        this.colLocation = 0;
        this.cash = 100;
        this.health = 100;
        this.equipmentMass = 0;
        this.equipment = new LinkedList<Equipment>();
        this.currentArea = inArea;
    }

    /* ==== GETTERS ==== */

    public int getRowLocation() {
        return rowLocation;
    }

    public int getColLocation() {
        return colLocation;
    }

    public int getCash() {
        return cash;
    }

    public double getHealth() {
        return health;
    }

    public UUID getId() {
        return mId;
    }

    public double getEquipmentMass() {
        equipmentMass = 0;
        for (Equipment equip : equipment) {
            equipmentMass += equip.getMass();
        }
        return equipmentMass;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public Area getCurrentArea() {
        return currentArea;
    }

    /* ==== SETTERS ==== */

    public void setRowLocation(int rowLocation) {
        this.rowLocation = rowLocation;
    }

    public void setColLocation(int colLocation) {
        this.colLocation = colLocation;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setEquipmentMass(double equipmentMass) {
        this.equipmentMass = equipmentMass;
    }

    public void addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        this.equipment.remove(equipment);
    }

    public void setCurrentArea(Area currentArea) {
        this.currentArea = currentArea;
    }

    public void setEquipment(List<Item> equipment) {
        for (Item i : equipment) {
            if (i instanceof Equipment) {
                this.equipment.add((Equipment) i);
            }
        }
    }
}
