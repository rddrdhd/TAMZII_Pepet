package com.example.thegathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myButton = findViewById(R.id.button);

        myButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) { //parametr Event pomocí něhož můžeme odchytávat co se stalo

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                     Toast.makeText(getApplicationContext(), "Action down", Toast.LENGTH_SHORT).show();
                 }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(getApplicationContext(), "Action up", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), OpenGLES20Activity.class);
                   // myIntent.putExtra("key", "value");

                    startActivity(myIntent);
                }

                return true;
            }
        });
    }
}
