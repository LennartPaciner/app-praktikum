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
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_recipe);


        textView = findViewById(R.id.textRezept);
        imageView = findViewById(R.id.imageRezept);
        button = findViewById(R.id.imageButton);

        Intent intent = getIntent();
        String descr = intent.getStringExtra(CookingView.EXTRA_DESC);
        String image = intent.getStringExtra(CookingView.EXTRA_IMAGE);

        loadImage(image);
        textView.setText(descr);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Müssen noch UI speichern ansonsten löscht er alles. Oder wir lassen den button weg und gehen übers handy zurück, das funktioniert.
                //goBack();

            }
        });
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

    //public void goBack(){

      //  Intent intent = new Intent(this, CookingView.class);
        //startActivity(intent);
    //}
}
