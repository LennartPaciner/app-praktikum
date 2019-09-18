package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EinkaufsListe extends AppCompatActivity {

    private EditText addItem;
    private int amount = 1;
    private SQLiteDatabase database;
    private Button add;
    private static final String TAG = "Einkaufsliste";
    private EinkaufsListeDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einkaufs_liste);

        EinkaufsListeDB einkaufsListe = new EinkaufsListeDB(this);
        database = einkaufsListe.getWritableDatabase();

        add = findViewById(R.id.button_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDB();

            }
        });
        //getProductAll(einkaufsListe.getAllData());
        getProductAll(einkaufsListe.getAllData());

    }

    public void increase(){
        amount++;

    }

    public void addItemDB(){
        if (addItem.getText().toString().trim().length() == 0 || amount == 0){
            return;
        }
        String name = addItem.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.GroceryEntry.COLUMN_ID,1);
        cv.put(DBHelper.GroceryEntry.COLUMN_BARCODE, 12345);
        cv.put(DBHelper.GroceryEntry.COLUMN_NAME, name);
        cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, 5);
        cv.put(DBHelper.GroceryEntry.COLUMN_MHD, "2008-11-11");
        cv.put(DBHelper.GroceryEntry.COLUMN_RESTOCK, 1);
        database.insert(DBHelper.GroceryEntry.TABLE_NAME1, null, cv);
        database.insert(DBHelper.GroceryEntry.TABLE_NAME2, null, cv);

        //addItem.getText().clear();
    }

    public void removeItemDB(long id){
        database.delete(DBHelper.GroceryEntry.TABLE_NAME1, DBHelper.GroceryEntry._ID + "=" + id, null);

    }

    public void updateItemDB(long id, ContentValues content){
        database.update(DBHelper.GroceryEntry.TABLE_NAME1, content, DBHelper.GroceryEntry._ID + "=" + id, null);
        //ContentValues con = new ContentValues();
        //con.put(DBHelper.GroceryEntry.COLUMN_NAME, "nudel");
        //con.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, 10);
        //updateItemDB(4, con);
    }

    public JSONArray getProductAll(Cursor result) {
        JSONArray resultSet = new JSONArray();
        StringBuffer buffer = new StringBuffer();
        if (result != null && result.getCount() > 0) {
            while (result.moveToNext()) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("id", result.getInt(0));
                    object.put("barcode", result.getInt(1));
                    object.put("name", result.getString(2));
                    object.put("menge", result.getInt(3));
                    object.put("mhd", result.getString(4));
                    object.put("restock", result.getInt(5));
                    resultSet.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        Toast.makeText(this, resultSet.toString(), Toast.LENGTH_LONG).show();
        return resultSet;
    }



}
