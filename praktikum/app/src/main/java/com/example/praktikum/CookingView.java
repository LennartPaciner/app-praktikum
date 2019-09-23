package com.example.praktikum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CookingView extends AppCompatActivity {
    private TextView textView;
    private RequestQueue mQueue;
    private String essenApi;
    private JSONArray jsonArray2;
    public static final String EXTRA_DESC = "intent_desc";
    public static final String EXTRA_IMAGE = "intent_image";
    public static final String EXTRA_MEAL = "intent_meal";
    public boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_view);
        flag = false;
        //textView = findViewById(R.id.text_view_result);
        Button buttonP = findViewById(R.id.button_parse);


        mQueue = Volley.newRequestQueue(this);
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    public void openDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.text_entry2, null);

        final EditText input = textEntryView.findViewById(R.id.edit3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.insert_food).setView(textEntryView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                essenApi = input.getText().toString();
                //Toast.makeText(getApplicationContext(), essenApi, Toast.LENGTH_LONG).show();
                if(!flag){
                    flag = true;
                    jsonParse(essenApi);
                }else{
                    clearView();
                    jsonParse(essenApi);
                }

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        builder.show();
    }

    public void clearView(){
        TableLayout tableLayout = findViewById(R.id.tableLayout1);
        TableRow tableRow = findViewById(R.id.rowLayout);
        tableLayout.removeAllViews();
        tableLayout.addView(tableRow);
    }

    //public String getText(){
    //EditText editText = findViewById(R.id.edit_text);
    //  String essen = editText.getText().toString();
    //Toast.makeText(this, essen, Toast.LENGTH_SHORT).show();
    //return essen;

    //}

    public void jsonParse(String essen) {
        //String url = "https://www.themealdb.com/api/json/v1/1/random.php";
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + essen;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            jsonArray2 = response.getJSONArray("meals");

                            createView(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);


    }

    public JSONArray createView(JSONArray arr) {

        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("strMeal");
                String category = object.getString("strCategory");
                String id = object.getString("idMeal");


                TableLayout tableLayout = findViewById(R.id.tableLayout1);
                TableRow rowLayout = findViewById(R.id.rowLayout);
                LinearLayout linearLayout = findViewById(R.id.linearLayout2);
                FrameLayout frameLayout = findViewById(R.id.frameLayoutProduct);
                TextView columnLayout = findViewById(R.id.columnLayout);

                TableRow neu = new TableRow(this);
                LinearLayout neuLL = new LinearLayout(this);
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
                amountTV.setText(category);
                amountFL.addView(amountTV);
                neuLL.addView(amountFL);


                // add Id
                FrameLayout idFL = new FrameLayout(this);
                TextView idTV = new TextView(this);
                idFL.setLayoutParams(frameLayout.getLayoutParams());
                idTV.setLayoutParams(columnLayout.getLayoutParams());
                idTV.setText(id);
                idFL.addView(idTV);
                neuLL.addView(idFL);

                // add button
                FrameLayout qrFL = new FrameLayout(this);
                final Button qrBtn = new Button(this);
                qrFL.setLayoutParams(frameLayout.getLayoutParams());
                qrBtn.setLayoutParams(columnLayout.getLayoutParams());
                int tmp = i+1;
                qrBtn.setText("Rezept " +tmp);
                qrFL.addView(qrBtn);
                neuLL.addView(qrFL);
                qrBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openRecipe(qrBtn.getText().toString());

                    }
                });

                neu.addView(neuLL);
                tableLayout.addView(neu);


            } catch (JSONException e) {
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        }
        return arr;
    }


    public void openRecipe(String btnS) {
        String zahl = "";
        int count = 0;
        for(char c : btnS.toCharArray()){
            if(count > 6){
                zahl+= c;
            }else{
                count += 1;
            }
        }
        int zahlI = Integer.parseInt(zahl);
        String description = getDescription(jsonArray2, zahlI);
        String imageApi = getImage(jsonArray2, zahlI);
        String meal = getMeal(jsonArray2, zahlI);

        Intent intent = new Intent(this, CookingRecipe.class);
        intent.putExtra(EXTRA_DESC, description);
        intent.putExtra(EXTRA_IMAGE, imageApi);
        intent.putExtra(EXTRA_MEAL, meal);

        startActivity(intent);
    }

    public String getDescription(JSONArray array, int zahl) {
            try {
                JSONObject object = array.getJSONObject(zahl-1);
                String descr = object.getString("strInstructions");
                return descr;
            } catch (JSONException e) {
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        return "Fehler";
    }

    public String getImage(JSONArray array, int zahl) {
            try {
                JSONObject object = array.getJSONObject(zahl-1);
                String image = object.getString("strMealThumb");
                return image;
            } catch (JSONException e) {
                Log.e("Einkaufsliste", e.getMessage());
                e.printStackTrace();
            }
        return "Fehler";
    }

    public String getMeal(JSONArray array, int zahl){
        try {
            JSONObject object = array.getJSONObject(zahl-1);
            String meal1 = object.getString("strMeal");
            return meal1;
        } catch (JSONException e) {
            Log.e("Einkaufsliste", e.getMessage());
            e.printStackTrace();
        }
        return "Fehler";
    }
}
