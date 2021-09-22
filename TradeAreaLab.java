package com.bignerdranch.android.tradesexplorer20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bignerdranch.android.tradesexplorer20.Model.Area;
import com.bignerdranch.android.tradesexplorer20.Model.GameData;
import com.bignerdranch.android.tradesexplorer20.TradeDbSchema.AreasTable;

public class TradeAreaLab {
    private SQLiteDatabase mDatabase;

    public TradeAreaLab() {}

    public void load(Context context)
    {
        mDatabase = new TradeBaseHelper(context).getWritableDatabase();
    }

    public void add(Area a) {
        ContentValues values = getContentValues(a);
        mDatabase.insert(AreasTable.NAME, null, values);
    }

    public List<Area> getAll() {
        List<Area> areas = new ArrayList<>();

        TradeCursorWrapper cursor = query(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                areas.add(cursor.getArea());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return areas;
    }

    public Area get(UUID id) {
        TradeCursorWrapper cursor = query(AreasTable.Cols.UUID + " = ?",new String[] {id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getArea();
        } finally {
            cursor.close();
        }
    }

    public void update(Area a) {
        String uuidString = a.getId().toString();
        ContentValues values = getContentValues(a);

        mDatabase.update(AreasTable.NAME, values, AreasTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private static ContentValues getContentValues(Area a) {
        ContentValues values = new ContentValues();
        values.put(AreasTable.Cols.UUID, a.getId().toString());
        values.put(AreasTable.Cols.XVALUE, a.getxValue());
        values.put(AreasTable.Cols.YVALUE, a.getyValue());
        values.put(AreasTable.Cols.DESC, a.getDescription());
        values.put(AreasTable.Cols.TOWN, a.isTown());
        values.put(AreasTable.Cols.EXPLORED, a.isExplored());
        values.put(AreasTable.Cols.STARRED, a.isStarred());

        return values;
    }

    private TradeCursorWrapper query(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                AreasTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TradeCursorWrapper(cursor);
    }

    public int size()
    {
        return query(null,null).getCount();
    }

    public Area[][] getGameMap(int size) {
        return new GameData(getAll(), size).getGrid();
    }
}
