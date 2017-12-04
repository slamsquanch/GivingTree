package com.example.zac.givingtreeapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.zac.givingtreeapp.notifications.NotificationReceiver;
import com.example.zac.givingtreeapp.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    /**
            * Firebase app instance
     */
    // FirebaseApp firebaseApp;

    /**
     * Firebase database instance
     */
    // private FirebaseDatabase database;

    /**
     * Firebase database reference
     */
    public static DatabaseReference databaseReference;

    /**
     * Firebase Database reference
     */
    public static DatabaseReference mDatabase;

    /**
     * Login token
     */
    public static String fcmToken = "";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LOCAL NOTIFICATION STUFF
        setContentView(R.layout.activity_main);
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curHr = calendar.get(Calendar.HOUR_OF_DAY);
        if (curHr >= 13) {
            // Since current hour is over 14, setting the date to the next day.
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,44);
        calendar.set(Calendar.SECOND, 50);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (savedInstanceState == null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        }*/

        fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("MYTAG", "This is your Firebase token : " + fcmToken);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        Map<String, Object> add = new HashMap<>();

        add.put("id", fcmToken);

        MainActivity.mDatabase.child("tokens").updateChildren(add);

        // Get data from our received notification
        Bundle bundle = getIntent().getExtras();
        String msg = "hello";
        if (bundle != null) {
            Toast.makeText(this, "BUNDLE NOT NULL", Toast.LENGTH_LONG).show();
            //bundle must contain all info sent in "data" field of the notification
                msg = bundle.getString("msg");
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        //Log.e("data: ", msg);
    }
}
