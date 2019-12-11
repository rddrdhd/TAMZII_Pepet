package com.example.thegathering.First;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thegathering.R;
import com.example.thegathering.Utils.Score;

import static android.content.ContentValues.TAG;

public class FirstActivity extends Activity {
    FirstGameView gw;
    private TextView scoreLabel, highScoreLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gw = new FirstGameView(this);

        setContentView(R.layout.activity_first);
        LinearLayout surface = (LinearLayout)findViewById(R.id.surfaceView);
        surface.addView(gw);

        scoreLabel = findViewById(R.id.scoreLabel1);
        highScoreLabel = findViewById(R.id.highScoreLabel1);
        highScoreLabel.setText("Hight score: "+String.valueOf(Score.firstGameRecord));


        gw.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                scoreLabel.setText("Score: "+String.valueOf(Score.firstGameRound));
                return false;
            }
        });
    }

    public void save(View view){

        int result = Score.firstGame;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("First",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
