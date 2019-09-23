package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockActivity extends AppCompatActivity {
    private Button einkaufButton;
    private Button homeButton;
    private Button recipeButton;
    private Button cookingButton;
    private Button addProduct;
    private SQLiteDatabase database;

    private EinkaufsListe einkaufsListe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        EinkaufsListeDB einkaufsListeDB = new EinkaufsListeDB(this);
        EinkaufsListe einkaufsListe2 = new EinkaufsListe();
        database = einkaufsListeDB.getWritableDatabase();
        einkaufsListe = einkaufsListe2;
        createVorratliste(einkaufsListe2.getProductAll(einkaufsListeDB.getAllData2()));


        einkaufButton = findViewById(R.id.buttonCheckList);
        einkaufButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEinkaufsListe();
            }
        });

        recipeButton = findViewById(R.id.buttonRecipe);
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCookingRecipe();
            }
        });

        cookingButton = findViewById(R.id.buttonCooking);
        cookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCookingView();
            }
        });


        homeButton = findViewById(R.id.buttonHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });

        addProduct = findViewById(R.id.buttonAddGrocery);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //should open window for adding items to Stock
            }
        });
    }

    public void createVorratliste(JSONArray array){
        JSONArray arr = array;
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

    public void removeItemDB(int id){
        database.delete(DBHelper.GroceryEntry.TABLE_NAME2, DBHelper.GroceryEntry.COLUMN_ID + "=" + id, null);

    }




    public void openEinkaufsListe(){
        Intent intent = new Intent(this, EinkaufsListe.class);
        startActivity(intent);
    }

    public void openCookingRecipe(){
        Intent intent = new Intent(this, CookingRecipe.class);
        startActivity(intent);
    }

    public void openCookingView(){
        Intent intent = new Intent(this, CookingView.class);
        startActivity(intent);
    }

    public void openMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
