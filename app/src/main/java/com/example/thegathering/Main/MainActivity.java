package com.example.thegathering.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thegathering.First.FirstActivity;
import com.example.thegathering.R;
import com.example.thegathering.Second.SecondActivity;
import com.example.thegathering.Third.ThirdActivity;
import com.example.thegathering.Utils.Score;

public class MainActivity extends AppCompatActivity {
    Pet pet;
    TextView tw0;
    TextView tw1;
    TextView tw2;
    TextView tw3;
    ImageView imgPet;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Score.firstGame = 0;

        Score.thirdGame = 0;

        tw0 = findViewById(R.id.textView0);
        tw1 = findViewById(R.id.textView1);
        tw2 = findViewById(R.id.textView2);
        tw3 = findViewById(R.id.textView3);
        imgPet = findViewById(R.id.imageView);

        pet = new Pet();

        //for timestamps
        final long[] t = new long[2];

        updateTextViews();

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
                updateTextViews();
                return true;
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10*1000; //Delay for 10 seconds.  One second = 1000 milliseconds.

    @Override
    protected void onResume() {

        if(Score.firstGame!=0){
            pet.cheer(Score.firstGame);
            Score.firstGame = 0;
        }
        if(Score.thirdGame!=0){
            pet.feed(Score.thirdGame/10);
            Score.thirdGame = 0;
        }

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
    public void firstAct(View view){
        //Intent i = new Intent(this, FullscreenActivity.class);
       // startActivityForResult(i, 1);
        Intent i = new Intent(this, FirstActivity.class);
        startActivity(i);
    }

    public void secondAct(View view){
        Intent i = new Intent(this, SecondActivity.class);
        startActivity(i);
    }

    public void thirdAct(View view){
        Intent i = new Intent(this, ThirdActivity.class);
       // startActivityForResult(i, 3);
        startActivity(i);
    }

    /* ****************************************************************************************** */
    public void updateTextViews(){

        if(Score.firstGame != 0){
            pet.cheer(Score.firstGame);
            Score.firstGame = 0;
        }
        if(Score.thirdGame != 0){
            pet.feed(Score.thirdGame/10);
            Score.thirdGame = 0;
        }
        tw0.setText("Love: "+pet.love());
        tw1.setText("Happ: "+pet.happy());
        tw2.setText("Fed:"+pet.fed());
        tw3.setText("Social: "+pet.social());
    }
}
