package com.example.praktikum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class EinkaufsListe extends AppCompatActivity {

    private EditText addItem;
    private int amount = 1;
    private SQLiteDatabase database;
    private Button add;
    private static final String TAG = "Einkaufsliste";
    public Button addProduct;
    public EinkaufsListeDB einkaufsListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einkaufs_liste);

        addProduct = findViewById(R.id.button_add);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox("Produktnamen eingeben:");
            }
        });

        einkaufsListe = new EinkaufsListeDB(this);
        database = einkaufsListe.getWritableDatabase();

        createEinkaufsliste();

    }

    public void increase(){
        amount++;

    }

    public void createEinkaufsliste(){
        JSONArray arr = getProductAll(einkaufsListe.getAllData());
        for(int i = 0; i < arr.length(); i++){
            try{
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("name");
                //int amount = object.getInt("amount");
                TableLayout tableLayout = findViewById(R.id.tableLayout1);
                TableRow rowLayout = findViewById(R.id.rowLayout);
                TextView columnLayout = findViewById(R.id.columnLayout);

                TableRow neu = new TableRow(this);

                TextView nameTV = new TextView(this);
                nameTV.setLayoutParams(columnLayout.getLayoutParams());
                TextView amountTV = new TextView(this);
                amountTV.setLayoutParams(columnLayout.getLayoutParams());
                CheckBox checkBox = new CheckBox(this);
                checkBox.setLayoutParams(columnLayout.getLayoutParams());
                Button qr = new Button(this);
                qr.setLayoutParams(columnLayout.getLayoutParams());


                nameTV.setText(name);
                amountTV.setText("1");

                checkBox.setText("");
                qr.setText("btn2");

                neu.addView(nameTV);
                neu.addView(amountTV);
                neu.addView(checkBox);
                neu.addView(qr);

                tableLayout.addView(neu);

            }catch (JSONException e){
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void addItemEinkaufsliste(@Nullable Long barcode, String name, @Nullable Integer amount, @Nullable String mhd, @Nullable Integer restock){

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
                    object.put("menge", result.getInt(3));
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
        // Creating alert Dialog with one Button
        final EditText edittext = new EditText(getApplicationContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EinkaufsListe.this);
        edittext.setGravity(Gravity.CENTER);
        alertDialog.setView(edittext);
        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Add Product");

        // Setting Dialog Message
        alertDialog.setMessage(text);

        // Setting Positive "Yes" Button
        alertDialog.setNeutralButton("ADD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        String product = edittext.getText().toString();
                        if(!checkNameInEinkaufsListe(product)){
                            addItemEinkaufsliste(null, product, null, null, null);
                            createEinkaufsliste();
                        }
                    }
                });
        alertDialog.show();
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
