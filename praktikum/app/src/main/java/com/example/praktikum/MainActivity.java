package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView testT;
    private long backPressedTime;
    private Toast backToast;
    private Button einkaufButton;
    private Button stockButton;
    private Button cookingButton;
    private Button recipeButton;
    private ToggleButton languageButton;
    public String appLanguage;
    private Button infoButton;
    private Button testB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        languageSetting();

        setContentView(R.layout.activity_main);

        createLanguageButton();    // create Language Toggle Button


        infoButton = findViewById(R.id.buttonInformation);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoView();
            }
        });


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
                openCookingView();
            }
        });


        stockButton = findViewById(R.id.buttonStock);
        stockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStockActivity();
            }
        });

        cookingButton = findViewById(R.id.buttonCooking);
        cookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCooking();
            }
        });
    }


    public void openEinkaufsListe(){
        setAppLanguage(appLanguage);
        Intent intent = new Intent(this, EinkaufsListe.class);
        startActivity(intent);
    }

    public void openCooking(){
        setAppLanguage(appLanguage);
        Intent intent = new Intent(this, Cooking.class);
        startActivity(intent);
    }

    public void openStockActivity(){
        setAppLanguage(appLanguage);
 
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
    }

    public void openInfoView(){
        setAppLanguage(appLanguage);
        Intent intent = new Intent(this, InformationView.class);
        startActivity(intent);
    }


    public void openCookingView(){
        setAppLanguage(appLanguage);
        Intent intent = new Intent(this, CookingView.class);
        startActivity(intent);
    }

    //Sprachenanpassung
    private void languageSetting(){
        if (getIntent().getStringExtra("startingLanguage") != null) {
            appLanguage = getResources().getConfiguration().locale.toString();
            getIntent().removeExtra("startingLanguage");
        }else{
            if (getIntent().getStringExtra("language") != null){
                appLanguage = getIntent().getStringExtra("language");
            }else{
                appLanguage = getResources().getConfiguration().locale.toString();     // returns active language code
            }
            setAppLanguage(appLanguage);
        }
    }


    public void setAppLanguage(String language) {
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(language));
        } else {
            config.locale = new Locale(language);
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }


    private void createLanguageButton(){
        languageButton = findViewById(R.id.toggleLanguage);

        if (appLanguage.startsWith("de")){
            languageButton.setChecked(true);
            appLanguage = "de";
        } else {
            languageButton.setChecked(false);
            appLanguage = "en";
        }

        languageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    appLanguage = "de";
                } else {
                    appLanguage = "en";
                }
                finish();
                getIntent().putExtra("language", appLanguage);
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }


    @Override
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            //super.onBackPressed();
            moveTaskToBack(true);
            return;
        } else{
            setAppLanguage(appLanguage);
            backToast=  Toast.makeText(getBaseContext(),R.string.press_back_again, Toast.LENGTH_SHORT);
           backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
