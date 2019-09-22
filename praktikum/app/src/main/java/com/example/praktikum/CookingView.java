package com.example.praktikum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_view);

        //textView = findViewById(R.id.text_view_result);
        Button buttonP = findViewById(R.id.button_parse);



        mQueue = Volley.newRequestQueue(this);
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //createView();

    }

    public void openDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.text_entry2, null);

        final EditText input = textEntryView.findViewById(R.id.edit3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Essen eingeben").setView(textEntryView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                essenApi = input.getText().toString();
                //Toast.makeText(getApplicationContext(), essenApi, Toast.LENGTH_LONG).show();
                jsonParse(essenApi);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        builder.show();
    }

    //public String getText(){
        //EditText editText = findViewById(R.id.edit_text);
      //  String essen = editText.getText().toString();
        //Toast.makeText(this, essen, Toast.LENGTH_SHORT).show();
        //return essen;

    //}

    public void jsonParse(String essen){
        //String url = "https://www.themealdb.com/api/json/v1/1/random.php";
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + essen;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");

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
        //Toast.makeText(getApplicationContext(), resultEssen.toString(), Toast.LENGTH_LONG).show();


    }

    public JSONArray createView(JSONArray arr){

        for(int i = 0; i < arr.length(); i++){
            try{
                JSONObject object = arr.getJSONObject(i);
                String name = object.getString("strMeal");
                String category = object.getString("strCategory");
                //int amount = object.getInt("amount");

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


                // add amount checkBox
                FrameLayout checkBoxFL = new FrameLayout(this);
                CheckBox checkBoxTV = new CheckBox(this);
                checkBoxFL.setLayoutParams(frameLayout.getLayoutParams());
                checkBoxTV.setLayoutParams(columnLayout.getLayoutParams());
                checkBoxFL.addView(checkBoxTV);
                neuLL.addView(checkBoxFL);

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
        return arr;
    }
}
