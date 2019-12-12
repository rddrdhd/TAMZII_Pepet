package com.example.thegathering.First;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gw = new FirstGameView(this);
        setContentView(R.layout.activity_first);
        LinearLayout surface = findViewById(R.id.surfaceView);
        surface.addView(gw);

        scoreLabel = findViewById(R.id.scoreLabel1);
        highScoreLabel = findViewById(R.id.highScoreLabel1);
        settings = getSharedPreferences("GAME_DATA_FIRST", Context.MODE_PRIVATE);
        Score.firstGameRecord = settings.getInt("HIGH_SCORE_FIRST", 0);
        highScoreLabel.setText("High Score : " + Score.firstGameRecord);

        settings = getSharedPreferences("GAME_DATA_FIRST", Context.MODE_PRIVATE);
        Score.firstGameRecord = settings.getInt("HIGH_SCORE_FIRST", 0);
        highScoreLabel.setText("High Score : " + Score.firstGameRecord);


        gw.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                if(Score.firstGameRound>Score.firstGameRecord){
                    Score.firstGameRecord = Score.firstGameRound;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("HIGH_SCORE_FIRST", Score.firstGameRecord);
                    editor.commit();
                    highScoreLabel.setText("High score: "+Score.firstGameRecord);

                }
                scoreLabel.setText("Score: "+String.valueOf(Score.firstGameRound));
                if(!start_flg){
                    startLayout.setVisibility(View.VISIBLE);
                }
                if(Score.firstGameRound > Score.firstGameRecord){
                    highScoreLabel.setText("High Score : " + Score.firstGameRecord);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("HIGH_SCORE_FIRST", Score.firstGameRecord);
                    editor.commit();
                }

                return false;
            }
        });

    /*public void save(View view){

     }

    public void startGame1(View view) {
        start_flg = true;
        startLayout.setVisibility(View.GONE);
    }
    public void quitGame1(View view) {
        end_flg = false;
        finish();
    }*/
}
