package com.example.thegathering.Main;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.thegathering.First.FirstActivity;
import com.example.thegathering.Fourth.FourthActivity;
import com.example.thegathering.R;
import com.example.thegathering.Second.SecondActivity;
import com.example.thegathering.Third.ThirdActivity;
import com.example.thegathering.Utils.Score;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    Pet pet;
    ProgressBar pb0, pb1, pb2, pb3, pb4;
    ImageView imgPet;
    Handler handler = new Handler();
    Runnable runnable;
    SharedPreferences settings;
    int delay = 10*1000; //Delay for 5 seconds


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        Score.firstGame = 0;
        Score.thirdGame = 0;

        imgPet = findViewById(R.id.imageView);
        pb0 = findViewById(R.id.progressBar0);
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        pb4 = findViewById(R.id.progressBar4);


        pet = new Pet();
        updateProgressBars();

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

        createPepeNotificationChannel();
        //createPepeNotification();
    }


    @Override
    protected void onResume() {
        Gson gson = new Gson();
        String json = settings.getString("PET", null);
        if (json!=null){
            pet = gson.fromJson(json, Pet.class);
        }
       updateProgressBars();
        //start handler as activity become visible
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                pet.decreaseStats();
                updateProgressBars();
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

    @Override
    protected void onStop() {
        Gson gson = new GsonBuilder().create();

        String json = gson.toJson(pet);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("PET", json);
        editor.apply();

        super.onStop();
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

    public void fourthAct(View view){
        Intent i = new Intent(this, FourthActivity.class);
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

        pb0.setProgress(pet.love());
        pb1.setProgress(pet.happy());
        pb3.setProgress(pet.fed());
        pb4.setProgress(pet.social());
    }

    /* ****************************************************************************************** */

    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";

    public void createPepeNotificationChannel(){
// Importance applicable to all the notifications in this Channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//Notification channel should only be created for devices running Android 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[] {
                    500,
                    500,
                    500,
                    500,
                    500
            });
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    public void createPepeNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Love me, human!");
        builder.setContentText("I would like to play some games w u");
        builder.setSmallIcon(R.drawable.happyicon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.happyicon));
        Notification notification = builder.build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(101, notification);

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, intent, 0);
//Following will set the tap action
        builder.setContentIntent(pendingIntent);
    }
}
