package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cooking extends AppCompatActivity {
    private Button einkaufButton;
    private Button recipeButton;
    private Button stockButton;
    private Button homeButton;
    private Button addVorrat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);


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
