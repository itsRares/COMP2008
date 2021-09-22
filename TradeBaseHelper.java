package com.bignerdranch.android.tradesexplorer20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.tradesexplorer20.TradeDbSchema.AreasTable;
import com.bignerdranch.android.tradesexplorer20.TradeDbSchema.ItemsTable;
import com.bignerdranch.android.tradesexplorer20.TradeDbSchema.PlayerTable;

public class TradeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TradeBase.db";

    public TradeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Areas table
        db.execSQL("create table " + AreasTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                AreasTable.Cols.UUID + ", " +
                AreasTable.Cols.XVALUE + ", " +
                AreasTable.Cols.YVALUE + ", " +
                AreasTable.Cols.DESC + ", " +
                AreasTable.Cols.TOWN + ", " +
                AreasTable.Cols.EXPLORED + ", " +
                AreasTable.Cols.STARRED +
                ")"
        );

        //Create Items table
        db.execSQL("create table " + ItemsTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ItemsTable.Cols.UUID + ", " +
                ItemsTable.Cols.TYPE + ", " +
                ItemsTable.Cols.TITLE + ", " +
                ItemsTable.Cols.DESC + ", " +
                ItemsTable.Cols.VALUE + ", " +
                ItemsTable.Cols.UNIQUE +
                ")"
        );

        //Create Player
        db.execSQL("create table " + PlayerTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                PlayerTable.Cols.UUID + ", " +
                PlayerTable.Cols.ROW + ", " +
                PlayerTable.Cols.COL + ", " +
                PlayerTable.Cols.CASH + ", " +
                PlayerTable.Cols.HEALTH + ", " +
                PlayerTable.Cols.EQMASS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

