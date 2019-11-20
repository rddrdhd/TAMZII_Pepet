package com.example.thegathering;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Pet pet;
    TextView tw1;
    TextView tw2;
    TextView tw3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw1 = findViewById(R.id.textView);
        tw2 = findViewById(R.id.textView2);
        tw3 = findViewById(R.id.textView3);


        pet = new Pet();

        tw1.setText("Happ: "+pet.statHappy);
        tw2.setText("Fed:"+pet.statFed);
        tw3.setText("Heal: "+pet.statHealth);
    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.


    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() { //Every 15 seconds
                //do something
                pet.statHealth--;
                pet.statFed--;
                pet.statHappy--;


                tw1.setText("Happ: "+pet.statHappy);
                tw2.setText("Fed:"+pet.statFed);
                tw3.setText("Heal: "+pet.statHealth);



                Toast.makeText(getApplicationContext(), "stats refreshed", Toast.LENGTH_SHORT).show();
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    public void fullscreenAct(View view){
        Intent i = new Intent(this, FullscreenActivity.class);
        startActivityForResult(i, 1);
    }
    public void secondAct(View view){
        Intent i = new Intent(this, SecondActivity.class);
        startActivityForResult(i, 2);
    }
    public void thirdAct(View view){
        Intent i = new Intent(this, ThirdActivity.class);
        startActivityForResult(i, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("First");

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "!onActivityResult!1", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("Second");

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "!onActivityResult!2", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("Third");

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "!onActivityResult!3", Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult
}
