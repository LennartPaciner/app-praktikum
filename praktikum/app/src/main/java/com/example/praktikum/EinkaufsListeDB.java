package com.example.praktikum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.praktikum.DBHelper.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.sql.Date;

import static java.sql.Types.BIT;

public class EinkaufsListeDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DB112.db";
    public static final int DATABASE_VERSION = 1;

    public EinkaufsListeDB(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EINKAUFSLISTE_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME1 + " (" +
                GroceryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ GroceryEntry.COLUMN_BARCODE +
                " TEXT, "  +GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, "  +
                GroceryEntry.COLUMN_AMOUNT + " TEXT DEFAULT '1', " + GroceryEntry.COLUMN_MHD + " TEXT, " +
                GroceryEntry.COLUMN_RESTOCK + " INTEGER);";
        db.execSQL(SQL_CREATE_EINKAUFSLISTE_TABLE);

        final String SQL_CREATE_VORRAT_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME2 + " (" + GroceryEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + GroceryEntry.COLUMN_BARCODE +
                " TEXT, "  +GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, "  +
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

    public JSONArray getStockDataJson(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * from " + GroceryEntry.TABLE_NAME2,null);

        JSONArray resultSet = new JSONArray();

        if (result != null && result.getCount() > 0) {
            while (result.moveToNext()) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("id", result.getInt(0));
                    object.put("barcode", result.getInt(1));
                    object.put("name", result.getString(2));
                    object.put("menge", result.getString(3));
                    object.put("mhd", result.getString(4));
                    object.put("restock", result.getInt(5));
                    resultSet.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return resultSet;
        }
        return resultSet;
    }

    public Cursor getNameData2(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + GroceryEntry.TABLE_NAME2 + " WHERE Name = " + "'" + name + "'", null);
    }

    public Cursor getNameData1(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + GroceryEntry.TABLE_NAME1 + " WHERE Name = " + "'" + name + "'", null);
    }

    public Cursor getIdData1(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + GroceryEntry.TABLE_NAME1 + " WHERE id = " + "'" + ID + "'", null);
    }

    public void addItemEListe(@Nullable String barcode, String name, @Nullable String amount, @Nullable String mhd, @Nullable Integer restock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBHelper.GroceryEntry.COLUMN_BARCODE, barcode);
        cv.put(DBHelper.GroceryEntry.COLUMN_NAME, name);
        //cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, amount);
        cv.put(DBHelper.GroceryEntry.COLUMN_MHD, mhd);
        cv.put(DBHelper.GroceryEntry.COLUMN_RESTOCK, restock);
        if (!checkNameInEinkaufsListe1(name)) {
            cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, amount);
            db.insert(DBHelper.GroceryEntry.TABLE_NAME1, null, cv);
        } else {
            JSONArray resultID = getProductAll(getNameData1(name));
            for (int i = 0; i < resultID.length(); i++) {
                try {
                    JSONObject object = resultID.getJSONObject(i);
                    final int iD = object.getInt("id");
                    float menge = Float.parseFloat(object.getString("menge"));
                    float ergebnis = menge + Float.parseFloat(amount);
                    cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, Float.toString(ergebnis));
                    updateItemEinkaufsliste(iD, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkNameInEinkaufsListe1(String Name){
        String sql = "SELECT * FROM " + DBHelper.GroceryEntry.TABLE_NAME1 + " WHERE Name = " + "'" + Name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    public JSONArray getProductAll(Cursor result) {
        JSONArray resultSet = new JSONArray();

        if (result != null && result.getCount() > 0) {
            while (result.moveToNext()) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("id", result.getInt(0));
                    object.put("barcode", result.getInt(1));
                    object.put("name", result.getString(2));
                    object.put("menge", result.getString(3));
                    object.put("mhd", result.getString(4));
                    object.put("restock", result.getInt(5));
                    resultSet.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return resultSet;
        }
        return resultSet;
    }

    public void updateItemEinkaufsliste(int id, ContentValues content){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DBHelper.GroceryEntry.TABLE_NAME1, content, DBHelper.GroceryEntry.COLUMN_ID + "=" + id, null);
    }

    public void updateItemVorrat(int id, ContentValues content){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DBHelper.GroceryEntry.TABLE_NAME2, content, DBHelper.GroceryEntry.COLUMN_ID + "=" + id, null);
    }


}
