package com.example.thegathering;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Pet pet;
    TextView tw1;
    TextView tw2;
    TextView tw3;
    ImageView imgPet;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw1 = findViewById(R.id.textView);
        tw2 = findViewById(R.id.textView2);
        tw3 = findViewById(R.id.textView3);
        imgPet = findViewById(R.id.imageView);

        pet = new Pet();

        updateTextViews();

        imgPet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Timestamp t1, t2;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //t1 = new Timestamp(new Date().getTime());
                    imgPet.setImageResource(R.drawable.happypepe);

                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //t2 = new Timestamp(new Date().getTime());
                    imgPet.setImageResource(R.drawable.smilepepe);
                    pet.cheer(2);
                    updateTextViews();
                }
                return true;
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10*1000; //Delay for 10 seconds.  One second = 1000 milliseconds.

    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() { //Every 10 seconds
                pet.decreaseStats();
                updateTextViews();

                Toast.makeText(getApplicationContext(), "stats refreshed", Toast.LENGTH_SHORT).show();
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }


    /* ****************************************************************************************** */
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


    /* ****************************************************************************************** */
    public void updateTextViews(){
        tw1.setText("Happ: "+pet.happy());
        tw2.setText("Fed:"+pet.fed());
        tw3.setText("Heal: "+pet.health());
    }
}
