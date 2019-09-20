package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button einkaufButton;
    private Button stockButton;
    private Button cookingButton;
    private ToggleButton languageButton;
    private String appLanguage;
    private Button infoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appLanguage = getResources().getConfiguration().locale.toString();     // returns active language code


        createLanguageButton();

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
                openCookingView();
            }
        });
    }

    public void openEinkaufsListe(){
        setAppLanguage(appLanguage);
        Intent intent = new Intent(this, EinkaufsListe.class);
        startActivity(intent);
    }

    public void openStockActivity(){
        setAppLanguage(appLanguage);
   //     System.out.println("Locale.getDefault().getLanguage():     "+Locale.getDefault().getLanguage());
   //     System.out.println("getResources().getConfiguration().locale:     "+getResources().getConfiguration().locale);
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


    private void setAppLanguage(String language) {
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
                    appLanguage = "en";                }
            }
        });

    }


}
