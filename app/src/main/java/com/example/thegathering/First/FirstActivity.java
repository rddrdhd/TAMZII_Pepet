package com.example.thegathering.First;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.thegathering.Main.Score;
import com.example.thegathering.R;

public class FirstActivity extends Activity {
    FirstGameView gw;

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
    }

    public void save(View view){

        int result = Score.firstGame;
        Intent returnIntent = new Intent();
        returnIntent.putExtra("First",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
