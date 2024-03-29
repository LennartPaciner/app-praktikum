package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
    public EinkaufsListeDB einkaufsListeDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        einkaufsListeDB = new EinkaufsListeDB(this);
        EinkaufsListe einkaufsListe2 = new EinkaufsListe();
        database = einkaufsListeDB.getWritableDatabase();
        einkaufsListe = einkaufsListe2;
        createVorratliste(einkaufsListe2.getProductAll(einkaufsListeDB.getAllData2()));


        einkaufButton = findViewById(R.id.buttonStock_CheckList);
        einkaufButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEinkaufsListe();
            }
        });

        recipeButton = findViewById(R.id.buttonStock_Recipe);
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCookingView();
            }
        });

        cookingButton = findViewById(R.id.buttonStock_Cooking);
        cookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCooking();
            }
        });


        homeButton = findViewById(R.id.buttonHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });

    }

    //Erstelle Ansicht mittels JSONArray der seine Daten per Cursor aus der DB bekommt.
    public void createVorratliste(JSONArray array){
        JSONArray arr = array;
        for(int i = 0; i < arr.length(); i++){
            try{
                final JSONObject object = arr.getJSONObject(i);
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
                amountTV.setText(amount);
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
                final Button qrBtn = new CheckBox(this);
                qrFL.setLayoutParams(frameLayout.getLayoutParams());
                qrBtn.setLayoutParams(columnLayout.getLayoutParams());
                qrFL.addView(qrBtn);
                final int tmp = i;

                neuLL.addView(qrFL);
                qrBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(((CheckBox) qrBtn).isChecked()){
                            try {
                                JSONArray jsonArray = einkaufsListeDB.getStockDataJson();
                                JSONObject object1 = jsonArray.getJSONObject(tmp);
                                String name = object1.getString("name");
                                String menge = object1.getString("menge");
                                einkaufsListeDB.addItemEListe(null, name, menge, null, null);

                                //Toast.makeText(StockActivity.this, object1.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.e("Einkaufsliste", e.getMessage());
                            }

                        }
                    }
                });

                neu.addView(neuLL);
                tableLayout.addView(neu);

            }catch (JSONException e){
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    //Lösche Item aus der Db und so auch aus der Ansicht.
    public void removeItemDB(int id){
        database.delete(DBHelper.GroceryEntry.TABLE_NAME2, DBHelper.GroceryEntry.COLUMN_ID + "=" + id, null);

    }




    public void openEinkaufsListe(){
        Intent intent = new Intent(this, EinkaufsListe.class);
        startActivity(intent);
    }

    public void openCooking(){
        Intent intent = new Intent(this, Cooking.class);
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
