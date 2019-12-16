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

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Pet pet;
    ProgressBar pb0, pb1, pb2, pb3, pb4;
    ImageView imgPet;
    Handler handler = new Handler();
    Runnable runnable;
    SharedPreferences settings;
    int delay = 10*1000; //Delay for 10 seconds
    boolean bad_flg, good_flg;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bad_flg = false;
        good_flg = false;

        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        Score.firstGame = 0;
        Score.secondGame = 0;
        Score.thirdGame = 0;
        Score.fourthGame = 0;

        imgPet = findViewById(R.id.imageView);
        pb0 = findViewById(R.id.progressBar0);
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        pb4 = findViewById(R.id.progressBar4);


        pet = fetchPet();
        updateProgressBars();

        //for timestamps
        final long[] t = new long[2];

        imgPet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    t[0] = System.currentTimeMillis();
                    int x = bad_flg ? R.drawable.pepe_sad   : R.drawable.pepe_happy;
                    x = pet.dead()  ? R.drawable.pepe_dead  : x;
                    imgPet.setImageResource(x);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    t[1] = System.currentTimeMillis();
                    int x = bad_flg ? R.drawable.pepe_cry   : R.drawable.pepe_smile;
                    x = pet.dead()  ? R.drawable.pepe_dead  : x;
                    imgPet.setImageResource(x);
                    if(pet.dead()) {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pet = new Pet();

                    }

                    pet.love((int)(t[1]-t[0])/666); //hold longer than 0.66 sec
                }
                updateProgressBars();
                return true;
            }
        });
        createPepeNotificationChannel();
    }


    @Override
    protected void onResume() {
        pet = fetchPet();
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
        SharedPreferences.Editor e = settings.edit();

        savePetStats(e);

        super.onStop();
    }

    /* ****************************************************************************************** */

    public void firstAct(View view) {
        Intent i = new Intent(this, FirstActivity.class);
        startActivity(i);
    }

    public void secondAct(View view) {
        Intent i = new Intent(this, SecondActivity.class);
        startActivity(i);
    }

    public void thirdAct(View view) {
        Intent i = new Intent(this, ThirdActivity.class);
        startActivity(i);
    }

    public void fourthAct(View view) {
        Intent i = new Intent(this, FourthActivity.class);
        startActivity(i);
    }

    /* ****************************************************************************************** */

    public void updateProgressBars() {

        if (Score.firstGame != 0) {
            pet.cheer(Score.firstGame);
            Score.firstGame = 0;
        }
        if (Score.secondGame != 0) {
            pet.exercise(Score.secondGame);
            Score.secondGame = 0;
        }
        if (Score.thirdGame != 0) {
            pet.feed(Score.thirdGame);
            Score.thirdGame = 0;
        }
        if (Score.fourthGame != 0) {
            pet.socialize(Score.fourthGame);
            Score.fourthGame = 0;
        }

        if (    pet.social() >90 &&
                pet.fed()    >90 &&
                pet.fit()    >90 &&
                pet.love()   >90 &&
                pet.happy()  >90 &&
                !good_flg   ) {
            good_flg = true;
            bad_flg = false;
            createPepeNotificationGood();
        }

        if (   (pet.social() <10 ||
                pet.fed()    <10 ||
                pet.fit()    <10 ||
                pet.love()   <10 ||
                pet.happy()  <10)&&
                !bad_flg    ) {
            good_flg = false;
            bad_flg = true;
            createPepeNotificationBad();
        }

        if(pet.social()+pet.fed()+pet.fit()+pet.love()+pet.happy()<10)
            pet.die();

        pb0.setProgress(pet.love());
        pb1.setProgress(pet.happy());
        pb2.setProgress(pet.fit());
        pb3.setProgress(pet.fed());
        pb4.setProgress(pet.social());
    }

    /* ****************************************************************************************** */

    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";

    public void createPepeNotificationChannel() {
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

    public void createPepeNotificationGood() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Thank you!");
        builder.setContentText("I can see you can take good care of me!.");
        builder.setSmallIcon(R.drawable.pepe_icon_happy);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.pepe_icon_happy));
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

    public void createPepeNotificationBad() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("I dont feel good!");
        builder.setContentText("Please take care of me!");
        builder.setSmallIcon(R.drawable.pepe_cry);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.pepe_cry));
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

    public void createPepeNotificationDead() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Your Pepe is dead!");
        builder.setContentText("Please take care of him next time!");
        builder.setSmallIcon(R.drawable.pepe_dead);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.pepe_dead));
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



    private void savePetStats(SharedPreferences.Editor e) {
        e.putInt("PET_FED",pet.fed());
        e.putInt("PET_HAPPY",pet.happy());
        e.putInt("PET_SOCIAL",pet.social());
        e.putInt("PET_LOVE",pet.love());
        e.putInt("PET_FIT",pet.fit());
        e.putString("PET_BORN",pet.getBorn());

        e.apply();
    }

    private Pet fetchPet() {
        pet = new Pet();
        pet.setStatFed(settings.getInt("PET_FED",pet.fed()));
        pet.setStatHappy(settings.getInt("PET_HAPPY",pet.happy()));
        pet.setStatSocial(settings.getInt("PET_SOCIAL",pet.social()));
        pet.setStatLove(settings.getInt("PET_LOVE",pet.love()));
        pet.setStatFit(settings.getInt("PET_FIT",pet.fit()));
        pet.setBorn(settings.getString("PET_BORN",pet.getBorn()));
        return pet;

    }
}
