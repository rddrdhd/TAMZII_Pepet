package com.example.thegathering.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thegathering.First.FirstActivity;
import com.example.thegathering.R;
import com.example.thegathering.Second.SecondActivity;
import com.example.thegathering.Third.ThirdActivity;
import com.example.thegathering.Utils.Score;

public class MainActivity extends AppCompatActivity {
    Pet pet;
    ProgressBar pb1, pb2, pb3, pb4;
    ImageView imgPet;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10*1000; //Delay for 5 seconds

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Score.firstGame = 0;
        Score.thirdGame = 0;

        imgPet = findViewById(R.id.imageView);
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        pb4 = findViewById(R.id.progressBar4);

        pet = new Pet();

        //for timestamps
        final long[] t = new long[2];


        imgPet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    t[0] = System.currentTimeMillis();
                    imgPet.setImageResource(R.drawable.happypepe);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    t[1] = System.currentTimeMillis();
                    imgPet.setImageResource(R.drawable.smilepepe);

                    pet.love((int)(t[1]-t[0])/666); //hold longer than 0.66 sec
                }
                updateProgressBars();
                return true;
            }
        });
        updateProgressBars();
    }


    @Override
    protected void onResume() {

       updateProgressBars();
        //start handler as activity become visible
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                pet.decreaseStats();
                updateProgressBars();

                //Toast.makeText(getApplicationContext(), "stats refreshed", Toast.LENGTH_SHORT).show();
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

    public void firstAct(View view){
        Intent i = new Intent(this, FirstActivity.class);
        startActivity(i);
    }

    public void secondAct(View view){
        Intent i = new Intent(this, SecondActivity.class);
        startActivity(i);
    }

    public void thirdAct(View view){
        Intent i = new Intent(this, ThirdActivity.class);
        startActivity(i);
    }

    /* ****************************************************************************************** */

    public void updateProgressBars(){

        if(Score.firstGame != 0){
            pet.cheer(Score.firstGame);
            Score.firstGame = 0;
        }
        if(Score.thirdGame != 0){
            pet.feed(Score.thirdGame);
            Score.thirdGame = 0;
        }

        pb1.setProgress(pet.love());
        pb2.setProgress(pet.happy());
        pb3.setProgress(pet.fed());
        pb4.setProgress(pet.social());
    }
}
