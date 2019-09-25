package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Cooking extends AppCompatActivity {
    private Button einkaufButton;
    private Button recipeButton;
    private Button stockButton;
    private Button homeButton;
    private Button addVorrat;
    public EinkaufsListe einkaufsListe;
    public EinkaufsListeDB einkaufsListeDB;

    String amount;
    EditText amountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        einkaufsListeDB = new EinkaufsListeDB(this);
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
        createEinkaufsliste();
    }

    public void createEinkaufsliste(){
        JSONArray arr = einkaufsListe.getProductAll(einkaufsListeDB.getAllData2());
        for(int i = 0; i < arr.length(); i++){
            try{
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("name");
                amount = object.getString("menge");
                //Toast.makeText(Cooking.this, name, Toast.LENGTH_LONG).show();
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

                // add amount checkBox
                FrameLayout deleteFL = new FrameLayout(this);
                Button deleteListe = new Button(this);
                deleteFL.setLayoutParams(frameLayout.getLayoutParams());
                deleteListe.setLayoutParams(columnLayout.getLayoutParams());
                //Drawable minusBtn = getResources().getDrawable(R.drawable.remove_cooking);
                //deleteListe.setBackground(minusBtn);
                deleteFL.addView(deleteListe);
                deleteListe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float menge = Float.parseFloat(amount);
                        menge = menge-1;
                        amount = Float.toString(menge);
                        setAmountTV(amount);

                    }
                });
                neuLL.addView(deleteFL);

                // add amount TV
                final FrameLayout amountFL = new FrameLayout(this);
                amountTV = new EditText(this);
                amountFL.setLayoutParams(frameLayout.getLayoutParams());
                amountTV.setLayoutParams(columnLayout.getLayoutParams());
                amountTV.setInputType(2);
                amountTV.setText(amount);
                amountFL.addView(amountTV);
                neuLL.addView(amountFL);

                // add qr
                FrameLayout qrFL = new FrameLayout(this);
                final Button qrBtn = new Button(this);
                qrFL.setLayoutParams(frameLayout.getLayoutParams());
                qrBtn.setLayoutParams(columnLayout.getLayoutParams());
                //Drawable plusBtn = getResources().getDrawable(R.drawable.add_cooking);
                //qrBtn.setBackground(plusBtn);
                qrBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float menge = Float.parseFloat(amount);
                        menge = menge+1;
                        amount = Float.toString(menge);
                        setAmountTV(amount);
                    }
                });
                final int tmp = i;
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

    public void setAmountTV(String a){
        amountTV.setText(a);
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
