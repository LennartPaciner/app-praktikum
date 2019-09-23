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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.w3c.dom.Text;

public class EinkaufsListe extends AppCompatActivity {

    private EditText addItem;
    private int amount = 1;
    private SQLiteDatabase database;
    private Button add;
    private static final String TAG = "Einkaufsliste";
    public Button addProduct;
    private static DecimalFormat df2 = new DecimalFormat("#.###");
    public EinkaufsListeDB einkaufsListe;
    private Button homeButton;
    private Button stockButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einkaufs_liste);

        addProduct = findViewById(R.id.buttonAddGrocery);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox("Produknamen eingeben:");
            }
        });

        einkaufsListe = new EinkaufsListeDB(this);
        database = einkaufsListe.getWritableDatabase();

        stockButton = findViewById(R.id.buttonStock);
        stockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStockActivity();
            }
        });



        homeButton = findViewById(R.id.buttonHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });


        createEinkaufsliste();

    }

    public void increase(){
        amount++;

    }

    public void createEinkaufsliste(){
        JSONArray arr = getProductAll(einkaufsListe.getAllData1());
        for(int i = 0; i < arr.length(); i++){
            try{
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("name");
                String amount = object.getString("menge");
                final int iD = object.getInt("id");

                final TableLayout tableLayout = findViewById(R.id.tableLayout1);
                TableRow rowLayout = findViewById(R.id.rowLayout);
                LinearLayout linearLayout = findViewById(R.id.linearLayout2);
                FrameLayout frameLayout = findViewById(R.id.frameLayoutProduct);
                TextView columnLayout = findViewById(R.id.columnLayout);

                final TableRow neu = new TableRow(this);
                final LinearLayout neuLL = new LinearLayout(this);
                neuLL.setLayoutParams(linearLayout.getLayoutParams());

                // add name TV
                FrameLayout nameFL = new FrameLayout(this);
                TextView nameTV = new TextView(this);
                nameFL.setLayoutParams(frameLayout.getLayoutParams());
                nameTV.setLayoutParams(columnLayout.getLayoutParams());
                nameTV.setText(name);
                nameFL.addView(nameTV);
                neuLL.addView(nameFL);

                // add amount TV
                FrameLayout amountFL = new FrameLayout(this);
                TextView amountTV = new TextView(this);
                amountFL.setLayoutParams(frameLayout.getLayoutParams());
                amountTV.setLayoutParams(columnLayout.getLayoutParams());
                amountTV.setText(amount + " gramm");
                amountFL.addView(amountTV);
                neuLL.addView(amountFL);


                // add amount checkBox
                FrameLayout deleteFL = new FrameLayout(this);
                Button deleteListe = new Button(this);
                deleteFL.setLayoutParams(frameLayout.getLayoutParams());
                deleteListe.setLayoutParams(columnLayout.getLayoutParams());
                deleteListe.setText("X");
                deleteFL.addView(deleteListe);
                neuLL.addView(deleteFL);
                deleteListe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeItemDB(iD);
                        neu.removeView(neuLL);
                        tableLayout.removeView(neu);
                    }
                });

                // add qr
                FrameLayout qrFL = new FrameLayout(this);
                Button qrBtn = new Button(this);
                qrFL.setLayoutParams(frameLayout.getLayoutParams());
                qrBtn.setLayoutParams(columnLayout.getLayoutParams());
                qrBtn.setText("QR");
                qrFL.addView(qrBtn);
                neuLL.addView(qrFL);

                neu.addView(neuLL);
                tableLayout.addView(neu);

            }catch (JSONException e){
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void addItemEinkaufsliste(@Nullable Long barcode, String name, @Nullable String amount, @Nullable String mhd, @Nullable Integer restock){

        ContentValues cv = new ContentValues();

        cv.put(DBHelper.GroceryEntry.COLUMN_BARCODE, barcode);
        cv.put(DBHelper.GroceryEntry.COLUMN_NAME, name);
        cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, amount);
        cv.put(DBHelper.GroceryEntry.COLUMN_MHD, mhd);
        cv.put(DBHelper.GroceryEntry.COLUMN_RESTOCK, restock);

        database.insert(DBHelper.GroceryEntry.TABLE_NAME1, null, cv);

        if (!checkNameInEinkaufsListe2(name)){
            database.insert(DBHelper.GroceryEntry.TABLE_NAME2, null,cv);
        }
        else{
            JSONArray resultID = getProductAll(einkaufsListe.getIdData2(name));
            for(int i = 0; i < resultID.length(); i++){
                try {
                    JSONObject object = resultID.getJSONObject(i);
                    Toast.makeText(this, object.toString(), Toast.LENGTH_LONG).show();
                    final int iD = object.getInt("id");
                    int menge = Integer.parseInt(object.getString("menge"));
                    int ergebnis = menge+ Integer.parseInt(amount);
                    String str = String.valueOf(ergebnis);
                    cv.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, str);
                    updateItemDB(iD, cv);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public void removeItemDB(int id){
        database.delete(DBHelper.GroceryEntry.TABLE_NAME1, DBHelper.GroceryEntry.COLUMN_ID + "=" + id, null);

    }

    public void updateItemDB(int id, ContentValues content){
        database.update(DBHelper.GroceryEntry.TABLE_NAME2, content, DBHelper.GroceryEntry.COLUMN_ID + "=" + id, null);
        //ContentValues con = new ContentValues();
        //con.put(DBHelper.GroceryEntry.COLUMN_NAME, "nudel");
        //con.put(DBHelper.GroceryEntry.COLUMN_AMOUNT, 10);
        //updateItemDB(4, con);
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

    public void showDialogBox(String text){

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.text_entry, null);

        final EditText input1 =  textEntryView.findViewById(R.id.edit1);
        final EditText input2 =  textEntryView.findViewById(R.id.edit2);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.product_and_amount).setView(textEntryView).setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        if(!checkNameInEinkaufsListe1(input1.toString())){
                            if(input2.getText().toString().equals("")){
                                addItemEinkaufsliste(null, input1.getText().toString(),null , null, null);


                                Intent intentEL = new Intent(EinkaufsListe.this, EinkaufsListe.class);
                                overridePendingTransition(0, 0);
                                startActivity(intentEL);
                                overridePendingTransition(0, 0);
                            }else{
                                float input = Float.parseFloat(input2.getText().toString());
                                String amount = df2.format(input);
                                addItemEinkaufsliste(null, input1.getText().toString(),amount , null, null);


                                Intent intentEL = new Intent(EinkaufsListe.this, EinkaufsListe.class);
                                overridePendingTransition(0, 0);
                                startActivity(intentEL);
                                overridePendingTransition(0, 0);
                            }
                        }
                    }
                }).setNegativeButton(R.string.cancel,
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

    public boolean checkNameInEinkaufsListe1(String Name){
        String sql = "SELECT * FROM " + DBHelper.GroceryEntry.TABLE_NAME1 + " WHERE Name = " + "'" + Name + "'";

        Cursor mCursor = database.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean checkNameInEinkaufsListe2(String Name){
        String sql = "SELECT * FROM " + DBHelper.GroceryEntry.TABLE_NAME2 + " WHERE Name = " + "'" + Name + "'";

        Cursor mCursor = database.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }


    public void openMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openStockActivity(){
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
    }


}
