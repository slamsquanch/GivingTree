package com.example.zac.givingtreeapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zac.givingtreeapp.classes.Day;
import com.example.zac.givingtreeapp.database.DBHelper;
import com.example.zac.givingtreeapp.notifications.NotificationReceiver;
import com.example.zac.givingtreeapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import com.google.firebase.messaging.RemoteMessage;

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

    private Bundle instanceState;

    private String messageToDisplay;


    private int today;

    private int buttons[] = new int[24];


    // Messages are already stored and sent from my PHP server script,
    // but this array is used as local backup for swapping current day's task.
    private String extraMessages[] = {
            "Give someone a hug",
            "Pay it Backward: buy coffee for the person behind you in line",
            "Offer to help you neighbours setup their Christmas decorations",
            "Offer to shovel someone's driveway or mow their lawn.",
            "Call your grandparents. Call them!",
            "Pickup and throw away the litter you see when you go outside today",
            "Help someone struggling with heavy bags",
            "Do the dishes even if it's your roommate's turn",
            "Wash someone's car.",
            "Dog or catsit for free",
            "Make two lunches and give one away",
            "Reduce air pollution by carpooling",
            "Say yes at the store when the cashier asks if you want to donate $1 to whichever cause",
            "Plant a tree",
            "Purchase some extra dog or cat food and drop it off at an animal shelter",
            "Bring a security guard a hot cup of coffee",
            "Tell your siblings how much you appreciate them",
            "Give up your seat to someone (anyone!) on the bus or subway",
            "Hold the door open for people today",
            "Stop to talk to a homeless person",
            "Donate your old eyeglasses so someone else can use them",
            "Bring delicious snacks or dessert to work for everyone",
            "Let someone go in front of you in line who only has a few items",
            "Talk to the shy person at work or school",
            "Cook a meal or do a load of laundry for a friend who just had a baby or is going through a difficult time",
            "Surprise a neighbor with freshly baked cookies or treats",
            "Compliment at laast three people today",
            "Say hi to the person next to you on the elevator",
            "Leave a gas gift card at a gas pump",
            "Leave quarters at the laundromat",
            "Have a LinkedIn account? Write a recommendation for coworker or connection",
            "Leave unused coupons next to corresponding products in the grocery store",
            "Try to make sure every person in a group conversation feels included",
            "Set an alarm on your phone to go off at three different times during the day. In those moments, do something kind for someone else",
            "Send a gratitude email to a coworker who deserves more recognition. Or tell them in person",
            "Know parents who could use a night out? Offer to babysit for free",
            "Return shopping carts for people at the grocery store",
            "Have a clean up party at a beach or park",
            "Everyone is important. Learn the names of your office security guard, the person at the front desk and other people you see every day",
            "Write your partner a list of things you love about them",
            "Keep an extra umbrella at work, so you can lend it out when it rains",
            "Send a ‘Thank you’ card or note to the officers at your local police or fire station",
            "Run an errand for a family member who is busy",
            "Leave a box of goodies in your mailbox for your mail carrier",
            "Tape coins around a playground for kids to find",
            "Put your phone away while in the company of others",
            "When you hear that discouraging voice in your head, tell yourself something positive — you deserve kindness too!",
            "Go through your closet and donate clothes that you no longer need to charity",
            "Say something positive to 5 people on social media today",
            "Offer to walk your neighbors dog",
            "Offer to cook a meal",
    };


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        today = DBHelper.getInstance(this).getLastDay();

        setContentView(R.layout.activity_main);
        //loadButtonImages();
        instanceState = savedInstanceState;

        fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("MYTAG", "This is your Firebase token : " + fcmToken);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        Map<String, Object> add = new HashMap<>();

        add.put("id", fcmToken);

        MainActivity.mDatabase.child("tokens").updateChildren(add);

        //GET ALL BUTTONS
        /*for (int i = 1; i < 24; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Log.e("BUTTONS", "button" + i);
            buttons[i] = id;
            //Log.e("BUTTONS", "" + id);
            //buttons[i] = (Button) findViewById(id);
        }*/


        ArrayList<Day> list = DBHelper.getInstance(this).getCalendar();
        Log.e("LIST_SIZE", list.size() + "");
        if (list != null) {
            for (Day d : list) {
                //Log.e("SQLite", d.getDay() + "");
                if (d.getMessage() != null)
                    Log.e("SQLite", d.getMessage());
                Log.e("SQLite", d.getID() + "");
                //Toast.makeText(this, "SQLite msgID: " + d.getID() + "", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "SQLite Msg: " + d.getMessage(), Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "SQLite Day: " + d.getDay() + "", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(this, "SQLite Day5: " + list.get(4).getDay() + "", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "SQLite Day5 Msg: " + list.get(4).getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Log.e("SQLite",  "NULL LIST");
        }

        // Get data from our received notification
        Bundle bundle = getIntent().getExtras();
        String msg = "hello";
        int msgID = -1;
        if (bundle != null) {
            //Toast.makeText(this, "BUNDLE NOT NULL", Toast.LENGTH_LONG).show();
            //bundle must contain all info sent in "data" field of the notification
                msg = bundle.getString("msg");
                String tmp = bundle.getString("msgID");
                msgID = Integer.parseInt(tmp);
            //Toast.makeText(this, "checking msg", Toast.LENGTH_LONG).show();
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
           // Toast.makeText(this, "msgID: " + msgID, Toast.LENGTH_LONG).show();  // WORKS!...

            //Log.e("msgID", msgID + "");  // WORKS!...
            //Log.e("msg", msg);         // WORKS!...
            // Add to local app database
            /*today = DBHelper.getInstance(this).getLastDay();
            Log.e("DAY", today + "");
            today++;
            Log.e("DAY++", today + "");
            if (today < 25) {
                DBHelper.getInstance(this).addMessage(
                        new Day(msgID, msg, today));
                Log.e("DAY++", today + "");
                Log.e("Main", "Message ADDED SUCCESSFULLY TO DB");
                swapDialog(msg, today);
            }
            else {
                Toast.makeText(this, "MERRY CHRISTMAS! \n Enjoy spending time with your loved ones! " + msgID, Toast.LENGTH_LONG).show();
            }*/
            storeMessage(msgID, msg);
            //LOAD BUTTON STATES
        }
        //Log.e("data: ", msg);
    }


    public void storeMessage(int msgID, final String msg) {
        today = DBHelper.getInstance(this).getLastDay();
        Log.e("DAY", today + "");
        today++;
        Log.e("DAY++", today + "");
        if (today < 25) {
            DBHelper.getInstance(this).addMessage(
                    new Day(msgID, msg, today));
            Log.e("DAY++", today + "");
            Log.e("Main", "Message ADDED SUCCESSFULLY TO DB");
                    swapDialog(msg, today);

        }
        else {
            Toast.makeText(this, "MERRY CHRISTMAS! \n Enjoy spending time with your loved ones! " + msgID, Toast.LENGTH_LONG).show();
        }
    }

    public void onButtonClick(View view) {
        String tag = view.getTag().toString();
        Log.e("TAG", tag);

        String[] tokens = tag.split("b");
        Log.e("SPLIT_TAG", tokens[1]);
        int day = Integer.parseInt(tokens[1]);
        setMessageToDisplay(DBHelper.getInstance(this).getDay(day));

        Log.e("Today", ""+today );
        if (day == today) {
            swapDialog(messageToDisplay, day);
        }
        viewMessage(messageToDisplay, day);

    }


    private void viewMessage(final String msg, final int day) {
        //runOnUiThread(new Runnable() {
           // public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Dec. " + day);

                builder.setMessage(msg);

                // Set up the buttons
                builder.setPositiveButton("DONE!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.setNeutralButton("Swap Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swapMessage(day);
                    }
                });

                builder.setNeutralButton("Set Reminder!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setReminder(msg);
                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.show();
        //    }
        //});
    }


    public void setMessageToDisplay(String msg) {
        this.messageToDisplay = msg;
    }


    public void swapMessage(int day) {
        // delete currently stored message for this day.
        DBHelper.getInstance(this).deleteMessage(day);
        Log.e("SWAP", "OG message deleted");

        // get a random new message and add to local database for this day
        Random rand = new Random();
        int index = rand.nextInt(extraMessages.length - 0 + 1) + 0;
        DBHelper.getInstance(this).addMessage(new Day(index, extraMessages[index], day));
        Log.e("SWAP", "new message added");
        Log.e("SWAP", extraMessages[index]);
    }

    public void setExtraMessages(String msg[]) {
        extraMessages = msg;
    }

    public int getExtraMsgSize() {
        return extraMessages.length;
    }


    private void notifyNow(String msg) {
        // LOCAL NOTIFICATION STUFF
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curHr = calendar.get(Calendar.HOUR_OF_DAY);
        int curMin = calendar.get(Calendar.MINUTE);
        int curSec = calendar.get(Calendar.SECOND);
        if (curHr >= 13) {
            // Since current hour is over 14, setting the date to the next day.
            calendar.add(Calendar.DATE, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, curHr);
        calendar.set(Calendar.MINUTE, curMin);
        calendar.set(Calendar.SECOND, curSec + 5);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        Intent intent1 = new Intent("my.action.string");
        intent1.putExtra("TITLE", "GivingTree local!");
        intent1.putExtra("MSG", msg);
        sendBroadcast(intent1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (instanceState == null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
        }
    }


    private void setReminder(String msg) {

        // LOCAL NOTIFICATION STUFF
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int curHr = calendar.get(Calendar.HOUR_OF_DAY);
        int curMin = calendar.get(Calendar.MINUTE);
        int curSec = calendar.get(Calendar.SECOND);
        if (curHr >= 13) {
            // Since current hour is over 14, setting the date to the next day.
            calendar.add(Calendar.DATE, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, curHr);
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 30);

        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        /*Intent intent1 = new Intent("data");
        intent1.putExtra("TITLE", "GivingTree Reminder!");
        intent1.putExtra("MSG", msg);
        sendBroadcast(intent1);*/

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (instanceState == null) {
            Log.e("reminder", "here!");
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 10000, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }



    public void swapDialog(final String msg, final int day) {
        runOnUiThread(new Runnable() {
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Dec. " + day);

                builder.setMessage(msg);

                // Set up the buttons
                builder.setPositiveButton("DONE!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.setNeutralButton("Swap Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swapMessage(day);
                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.show();
            }
        });
    }


    private void loadButtonImages() {
        ArrayList<Day> list = DBHelper.getInstance(this).getCalendar();
        Log.e("LIST_SIZE", list.size() + "");
        String id = "b";
        /*if (list != null) {
            Log.e("LoadButtons", "" + today);
            for (int i = today; i < 24; i++) {
                id = "button" + i;
                Button button = (Button)findViewById(R.id.button16);
                //button.setBackgroundResource(R.drawable.button2);
                button.setEnabled(true);
            }
            //setContentView(R.layout.activity_main);
        }
        else
            Log.e("LoadButtons", "list is null"); */
    }
}
