package com.bignerdranch.android.tradesexplorer20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bignerdranch.android.tradesexplorer20.Model.Equipment;
import com.bignerdranch.android.tradesexplorer20.Model.Food;
import com.bignerdranch.android.tradesexplorer20.Model.Item;
import com.bignerdranch.android.tradesexplorer20.TradeDbSchema.ItemsTable;

public class TradeItemLab {

    private SQLiteDatabase mDatabase;

    public TradeItemLab() {}

    public void load(Context context)
    {
        mDatabase = new TradeBaseHelper(context).getWritableDatabase();
    }

    public void add(Item i) {
        ContentValues values = getContentValues(i);
        mDatabase.insert(ItemsTable.NAME, null, values);
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();

        TradeCursorWrapper cursor = query(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return items;
    }

    public Item get(UUID id) {
        TradeCursorWrapper cursor = query(ItemsTable.Cols.UUID + " = ?",new String[] {id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();
        } finally {
            cursor.close();
        }
    }

    public int itemCount(UUID id)
    {
        return query(ItemsTable.Cols.UUID + " = ?",new String[] {id.toString()}).getCount();
    }

    public void update(Item i) {
        String uuidString = i.getId().toString();
        ContentValues values = getContentValues(i);

        mDatabase.update(ItemsTable.NAME, values, ItemsTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public void remove(Item item)
    {
        String id = item.getId().toString();
        mDatabase.delete(ItemsTable.NAME,ItemsTable.Cols.UUID + " = ?", new String[] {id});
    }

    public void removeAll() {
        mDatabase.execSQL("delete from "+ ItemsTable.NAME);
    }

    public int size()
    {
        return query(null,null).getCount();
    }

    private static ContentValues getContentValues(Item i) {
        ContentValues values = new ContentValues();
        values.put(ItemsTable.Cols.UUID, i.getId().toString());
        values.put(ItemsTable.Cols.TYPE, i.getType());
        values.put(ItemsTable.Cols.TITLE, i.getTitle());
        values.put(ItemsTable.Cols.DESC, i.getDescription());
        values.put(ItemsTable.Cols.VALUE, i.getValue());
        if (i instanceof Food) {
            values.put(ItemsTable.Cols.UNIQUE, ((Food) i).getHealth());
        } else {
            values.put(ItemsTable.Cols.UNIQUE, ((Equipment) i).getMass());
        }

        return values;
    }

    private TradeCursorWrapper query(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemsTable.NAME,
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
