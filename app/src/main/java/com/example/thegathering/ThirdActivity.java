package com.example.thegathering;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }

    public void back(View view){

        String result="from third";

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Third",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
