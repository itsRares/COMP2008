package com.bignerdranch.android.tradesexplorer20;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.Equipment;
import com.bignerdranch.android.tradesexplorer20.Model.Food;
import com.bignerdranch.android.tradesexplorer20.Model.Item;
import com.bignerdranch.android.tradesexplorer20.Model.Player;

import java.util.UUID;

public class TradeCursorWrapper extends CursorWrapper {
    public TradeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Player getPlayer() {
        String uuidString = getString(getColumnIndex(TradeDbSchema.PlayerTable.Cols.UUID));
        int rowLocation = getInt(getColumnIndex(TradeDbSchema.PlayerTable.Cols.ROW));
        int colLocation = getInt(getColumnIndex(TradeDbSchema.PlayerTable.Cols.COL));
        double cash = getDouble(getColumnIndex(TradeDbSchema.PlayerTable.Cols.CASH));
        double health = getDouble(getColumnIndex(TradeDbSchema.PlayerTable.Cols.HEALTH));
        double equipmentMass = getDouble(getColumnIndex(TradeDbSchema.PlayerTable.Cols.EQMASS));

        Player p = new Player(UUID.fromString(uuidString));
        p.setRowLocation(rowLocation);
        p.setColLocation(colLocation);
        p.setCash((int)cash);
        p.setHealth(health);
        p.setEquipmentMass(equipmentMass);

        return p;
    }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(TradeDbSchema.ItemsTable.Cols.UUID));
        String type = getString(getColumnIndex(TradeDbSchema.ItemsTable.Cols.TYPE));
        String title = getString(getColumnIndex(TradeDbSchema.ItemsTable.Cols.TITLE));
        String desc = getString(getColumnIndex(TradeDbSchema.ItemsTable.Cols.DESC));
        int value = getInt(getColumnIndex(TradeDbSchema.ItemsTable.Cols.VALUE));
        double unique = getDouble(getColumnIndex(TradeDbSchema.ItemsTable.Cols.UNIQUE));

        if (type.equals("food")) {
            Food i = new Food(UUID.fromString(uuidString));
            i.setTitle(title);
            i.setDescription(desc);
            i.setValue(value);
            i.setHealth(unique);
            return i;
        } else {
            Equipment i = new Equipment(UUID.fromString(uuidString));
            i.setTitle(title);
            i.setDescription(desc);
            i.setValue(value);
            i.setMass(unique);
            return i;
        }
    }

    public Area getArea() {
        String uuidString = getString(getColumnIndex(TradeDbSchema.AreasTable.Cols.UUID));
        int xValue = getInt(getColumnIndex(TradeDbSchema.AreasTable.Cols.XVALUE));
        int yValue = getInt(getColumnIndex(TradeDbSchema.AreasTable.Cols.YVALUE));
        String desc = getString(getColumnIndex(TradeDbSchema.AreasTable.Cols.DESC));
        int town = getInt(getColumnIndex(TradeDbSchema.AreasTable.Cols.TOWN));
        int explored = getInt(getColumnIndex(TradeDbSchema.AreasTable.Cols.EXPLORED));
        int starred = getInt(getColumnIndex(TradeDbSchema.AreasTable.Cols.STARRED));

        Area a = new Area(UUID.fromString(uuidString));
        a.setxValue(xValue);
        a.setyValue(yValue);
        a.setDescription(desc);
        a.setTown(town != 0);
        a.setExplored(explored != 0);
        a.setStarred(starred != 0);

        return a;
    }
}
