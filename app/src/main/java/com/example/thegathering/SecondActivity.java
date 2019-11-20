package com.example.thegathering;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    //tmp
    public void save(View view){

        String result="from second";

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Second",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
