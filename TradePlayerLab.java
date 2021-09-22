package com.bignerdranch.android.tradesexplorer20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.tradesexplorer20.Model.Player;
import com.bignerdranch.android.tradesexplorer20.TradeDbSchema.PlayerTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradePlayerLab {

    private SQLiteDatabase mDatabase;

    public TradePlayerLab() {}

    public void load(Context context)
    {
        mDatabase = new TradeBaseHelper(context).getWritableDatabase();
    }

    public void add(Player p) {
        ContentValues values = getContentValues(p);
        mDatabase.insert(PlayerTable.NAME, null, values);
    }

    public List<Player> getAll() {
        List<Player> players = new ArrayList<>();

        TradeCursorWrapper cursor = query(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                players.add(cursor.getPlayer());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return players;
    }

    public Player get() {
        TradeCursorWrapper cursor = query(null,null);

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPlayer();
        } finally {
            cursor.close();
        }
    }

    public void update(Player p) {
        String uuidString = p.getId().toString();
        ContentValues values = getContentValues(p);

        mDatabase.update(PlayerTable.NAME, values, PlayerTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public int size()
    {
        return query(null,null).getCount();
    }

    private static ContentValues getContentValues(Player p) {
        ContentValues values = new ContentValues();
        values.put(PlayerTable.Cols.UUID, p.getId().toString());
        values.put(PlayerTable.Cols.ROW, p.getRowLocation());
        values.put(PlayerTable.Cols.COL, p.getColLocation());
        values.put(PlayerTable.Cols.CASH, p.getCash());
        values.put(PlayerTable.Cols.HEALTH, p.getHealth());
        values.put(PlayerTable.Cols.EQMASS, p.getEquipmentMass());

        return values;
    }

    private TradeCursorWrapper query(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PlayerTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TradeCursorWrapper(cursor);
    }
}
