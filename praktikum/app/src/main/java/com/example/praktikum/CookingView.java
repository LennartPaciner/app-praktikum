package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_view);

        textView = findViewById(R.id.text_view_result);
        Button button = findViewById(R.id.button_parse);



        mQueue = Volley.newRequestQueue(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse(getText());
            }
        });

    }

    public String getText(){
        EditText editText = findViewById(R.id.edit_text);
        String essen = editText.getText().toString();
        Toast.makeText(this, essen, Toast.LENGTH_SHORT).show();
        return essen;

    }

    public void jsonParse(String essen){
        //String url = "https://www.themealdb.com/api/json/v1/1/random.php";
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + essen;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for(int i =0; i < jsonArray.length();i++){
                                JSONObject meal = jsonArray.getJSONObject(i);
                                int id = meal.getInt("idMeal");
                                String smeal = meal.getString("strMeal");

                                textView.append(id + ", " + smeal + "\n\n");
                            }
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
}
