package com.example.praktikum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.praktikum.DBHelper.*;

import org.json.JSONArray;

import java.math.BigInteger;
import java.sql.Date;

import static java.sql.Types.BIT;

public class EinkaufsListeDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DB12.db";
    public static final int DATABASE_VERSION = 1;

    public EinkaufsListeDB(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EINKAUFSLISTE_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME1 + " (" +
                GroceryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ GroceryEntry.COLUMN_BARCODE +
                " INTEGER, "  +GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, "  +
                GroceryEntry.COLUMN_AMOUNT + " TEXT DEFAULT '1', " + GroceryEntry.COLUMN_MHD + " TEXT, " +
                GroceryEntry.COLUMN_RESTOCK + " INTEGER);";
        db.execSQL(SQL_CREATE_EINKAUFSLISTE_TABLE);

        final String SQL_CREATE_VORRAT_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME2 + " (" + GroceryEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + GroceryEntry.COLUMN_BARCODE +
                " INTEGER, "  +GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, "  +
                GroceryEntry.COLUMN_AMOUNT + " TEXT DEFAULT '1', " + GroceryEntry.COLUMN_MHD + " TEXT, " +
                GroceryEntry.COLUMN_RESTOCK + " INTEGER);";
        db.execSQL(SQL_CREATE_VORRAT_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME2);
        onCreate(db);

    }

    public Cursor getAllData1(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * from " + GroceryEntry.TABLE_NAME1,null);

    }

    public Cursor getAllData2(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * from " + GroceryEntry.TABLE_NAME2,null);

    }

    public Cursor getIdData2(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + GroceryEntry.TABLE_NAME2 + " WHERE Name = " + "'" + name + "'", null);
    }

}