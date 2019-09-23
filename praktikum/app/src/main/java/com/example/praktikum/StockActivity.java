package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StockActivity extends AppCompatActivity {
    private Button einkaufButton;
    private Button homeButton;
    private Button recipeButton;
    private Button cookingButton;
    private Button addProduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


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




    public void openEinkaufsListe(){
        Intent intent = new Intent(this, EinkaufsListe.class);
        startActivity(intent);
    }

    public void openCookingRecipe(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCookingView(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
