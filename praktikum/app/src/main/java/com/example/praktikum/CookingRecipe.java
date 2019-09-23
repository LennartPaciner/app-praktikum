package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CookingRecipe extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_recipe);


        textView = findViewById(R.id.textRezept);
        imageView = findViewById(R.id.imageRezept);
        textView2 = findViewById(R.id.imageText);

        Intent intent = getIntent();
        String descr = intent.getStringExtra(CookingView.EXTRA_DESC);
        String image = intent.getStringExtra(CookingView.EXTRA_IMAGE);
        String meal = intent.getStringExtra(CookingView.EXTRA_MEAL);

        loadImage(image);
        textView.setText(descr);
        textView2.setText(meal);

    }

    public void loadImage(String url){
        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

}
