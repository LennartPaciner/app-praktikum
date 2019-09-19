package com.example.praktikum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class EinkaufsListe extends AppCompatActivity {

    private EditText addItem;
    private int amount = 1;
    private SQLiteDatabase database;
    private Button add;
    private static final String TAG = "Einkaufsliste";
    public Button addProduct;
    private static DecimalFormat df2 = new DecimalFormat("#.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einkaufs_liste);

        addProduct = findViewById(R.id.button_add);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showDialogBox("Produknamen eingeben:");
            }
        });

        EinkaufsListeDB einkaufsListe = new EinkaufsListeDB(this);
        database = einkaufsListe.getWritableDatabase();

        getProductAll(einkaufsListe.getAllData());
    }

    public void increase(){
        amount++;

    }

    public void addItemEinkaufsliste(@Nullable Long barcode, String name, @Nullable String amount, @Nullable String mhd, @Nullable Integer restock){

        ContentValues cv = new ContentValues();
        //cv.put(DBHelper.GroceryEntry.COLUMN_ID,1);
        cv.put(DBHelper.GroceryEntry.COLUMN_BARCODE, barcode);
        cv.put(DBHelper.GroceryEntry.COLUMN_NAME, name);
        cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, amount);
        cv.put(DBHelper.GroceryEntry.COLUMN_MHD, mhd);
        cv.put(DBHelper.GroceryEntry.COLUMN_RESTOCK, restock);
        database.insert(DBHelper.GroceryEntry.TABLE_NAME1, null, cv);

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
                    object.put("menge", result.getString(3));
                    object.put("mhd", result.getString(4));
                    object.put("restock", result.getInt(5));
                    resultSet.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
	Toast.makeText(this, resultSet.toString(), Toast.LENGTH_LONG).show();
        return resultSet;
        }
        Toast.makeText(this, resultSet.toString(), Toast.LENGTH_LONG).show();
        return resultSet;
    }

    public void showDialogBox(String text){

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.text_entry, null);

        final EditText input1 =  textEntryView.findViewById(R.id.edit1);
        final EditText input2 =  textEntryView.findViewById(R.id.edit2);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Produktname und Menge:").setView(textEntryView).setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        if(!checkNameInEinkaufsListe(input1.toString())){
                            float input = Float.parseFloat(input2.getText().toString());
                            String amount = df2.format(input);
                            addItemEinkaufsliste(null, input1.getText().toString(),amount , null, null);
                        }
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        return;
                    }
                });
        alert.show();
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public boolean checkNameInEinkaufsListe(String Name){
        String sql = "SELECT * FROM " + DBHelper.GroceryEntry.TABLE_NAME1 + " WHERE Name = " + "'" + Name + "'";

        Cursor mCursor = database.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }



}
