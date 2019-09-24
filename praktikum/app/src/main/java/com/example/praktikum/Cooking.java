package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLData;

public class Cooking extends AppCompatActivity {
    private Button einkaufButton;
    private Button recipeButton;
    private Button stockButton;
    private Button homeButton;
    private Button addVorrat;
    private EinkaufsListe einkaufsListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        EinkaufsListeDB einkaufsListeDB = new EinkaufsListeDB(this);
        EinkaufsListe einkaufsListe2 = new EinkaufsListe();
        SQLiteDatabase database = einkaufsListeDB.getWritableDatabase();
        einkaufsListe = einkaufsListe2;



        einkaufButton = findViewById(R.id.buttonCooking_CheckList);
        einkaufButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEinkaufsListe();
            }
        });

        recipeButton = findViewById(R.id.buttonCooking_Recipe);
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCookingView();
            }
        });

        stockButton = findViewById(R.id.buttonCooking_Stock);
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

        addVorrat = findViewById(R.id.buttonAddGrocery);
        addVorrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStockActivity();
            }
        });

        createCookingView(einkaufsListe2.getProductAll(einkaufsListeDB.getAllData2()));

    }

    public void createCookingView(JSONArray array){
        final ScrollView SV = findViewById(R.id.SView_Cooking);
        //All Views needed for Layout copy
        final TableLayout TL = findViewById(R.id.tableLayout_Cooking);
        final TableRow TR = findViewById(R.id.rowLayout_Cooking);
        final LinearLayout LL = findViewById(R.id.LL_Cooking);
        final FrameLayout FL_Product = findViewById(R.id.FL_Cooking_Product);
        final FrameLayout FL_Increase = findViewById(R.id.FL_Cooking_increase);
        final FrameLayout FL_Amount = findViewById(R.id.FL_Cooking_Amount);
        final FrameLayout FL_Decrease = findViewById(R.id.FL_Cooking_decrease);
        final TextView TV_Product = findViewById(R.id.columnLayout_Cooking_Product);
        final TextView TV_Amount = findViewById(R.id.columnLayout_Cooking_Amount);
        final TextView TV_Increase = findViewById(R.id.TV_Cooking_increase);
        final TextView TV_Decrease = findViewById(R.id.TV_Cooking_decrease);
        Toast.makeText(this,"CreateCooking",Toast.LENGTH_LONG).show();

        JSONArray arr = array;
        for(int i = 0; i < arr.length(); i++) {
            try {
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("name");
                String amount = object.getString("menge");
                final int iD = object.getInt("id");
                Toast.makeText(this, "Loop" + i, Toast.LENGTH_LONG).show();
                //Add next Row
                TableRow new_TableRow = new TableRow(this);
                LinearLayout new_LinearLayout = new LinearLayout(this);
                new_LinearLayout.setLayoutParams(LL.getLayoutParams());

                //Add Product
                FrameLayout product_FL = new FrameLayout(this);
                TextView product_TV = new TextView(this);
                product_FL.setLayoutParams(FL_Product.getLayoutParams());
                product_TV.setLayoutParams(TV_Product.getLayoutParams());
                product_TV.setText(name);
                product_FL.addView(product_TV);
                new_LinearLayout.addView(product_FL);

                //Add IncreaseButton
                FrameLayout increase_FL = new FrameLayout(this);
                Button increase_B = new Button(this);
                increase_FL.setLayoutParams(FL_Increase.getLayoutParams());
                increase_B.setLayoutParams(TV_Increase.getLayoutParams());
                increase_B.setText("+");
                increase_FL.addView(increase_B);
                new_LinearLayout.addView(increase_FL);
                increase_B.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //increase Amount by one
                    }
                });

                //Add Amount
                FrameLayout amount_FL = new FrameLayout(this);
                TextView amount_TV = new EditText(this);
                amount_FL.setLayoutParams(FL_Amount.getLayoutParams());
                amount_TV.setLayoutParams(TV_Amount.getLayoutParams());
                amount_TV.setText(amount);
                amount_FL.addView(amount_TV);
                new_LinearLayout.addView(amount_FL);

                //Add DecreaseButton
                FrameLayout decrease_FL = new FrameLayout(this);
                Button decrease_B = new Button(this);
                decrease_FL.setLayoutParams(FL_Decrease.getLayoutParams());
                decrease_B.setLayoutParams(TV_Decrease.getLayoutParams());
                decrease_B.setText("+");
                decrease_FL.addView(decrease_B);
                new_LinearLayout.addView(decrease_FL);
                decrease_B.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //decrease amount by one
                    }
                });


                new_TableRow.addView(new_LinearLayout);
                TL.addView(new_TableRow);


            } catch (JSONException e) {
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void openEinkaufsListe(){
        Intent intent = new Intent(this, EinkaufsListe.class);
        startActivity(intent);

    }
    public void openCookingView(){
        Intent intent = new Intent(this, CookingView.class);
        startActivity(intent);

    }
    public void openStockActivity(){
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);

    }
    public void openMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
