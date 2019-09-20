package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    private Button changeColorButton;
    private TextView textName;
    private int taps = 0;
    private int nextColor;
    private ConstraintLayout layout;
    private String[] colors = new String[]{
          //  "#D2FAFB",  // light blue
            "#C93838",  // dark red
            "#e5d429",  // gold
            "#b030b0",  // purple
            "#01d28e",  // green
            "#FB0091",  // pink
            "#000000"}; // black


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


        layout = findViewById(R.id.constraintLayout);

        textName = findViewById(R.id.textAppName);


        changeColorButton = findViewById(R.id.splashButton);

        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackgroundColor();

                nextColor = taps++ % colors.length;
            }
        });
    }

    public void changeBackgroundColor(){
        if ( colors[nextColor].equals("#000000")){
            textName.setTextColor(Color.parseColor("#FFFFFF"));
        } else{
            textName.setTextColor(Color.parseColor("#000000"));
        }
        layout.setBackgroundColor(Color.parseColor(colors[nextColor]));
    }
}
