package com.example.thegathering.First;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thegathering.R;
import com.example.thegathering.Utils.Score;

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


        //setContentView(gw);
        setContentView(R.layout.activity_first);
        LinearLayout surface = (LinearLayout)findViewById(R.id.surfaceView);
        surface.addView(gw);

        scoreLabel = findViewById(R.id.scoreLabel1);
        highScoreLabel = findViewById(R.id.highScoreLabel1);
    }

    public void save(View view){

        int result = Score.firstGame;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("First",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
