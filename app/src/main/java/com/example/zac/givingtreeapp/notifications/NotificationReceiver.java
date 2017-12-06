package com.example.zac.givingtreeapp.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.zac.givingtreeapp.R;
import com.example.zac.givingtreeapp.activity.MainActivity;

/**
 * Created by Zac on 2017-11-28.
 */

public class NotificationReceiver extends BroadcastReceiver {


    private String message = "Don't forget to perform your daily act of kindeness :)";
    private String title = "Reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*String action = intent.getAction();

        Log.e("Receiver", "Broadcast received: " + action);

        if (action != null) {
            if (action.equals("my.action.string")) {
                title = intent.getExtras().getString("TITLE");
                message = intent.getExtras().getString("MSG");

                Log.e("Receiver", "broadcast received!!");
                Log.e("Receiver", message);
            }
        }
        else Log.e("Receiver", "action is null"); */




            NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //if we want ring on notifcation then uncomment below line
        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                setSmallIcon(R.drawable.ic_star_icon).
                setContentIntent(pendingIntent).
                    setContentText(message).
                setContentTitle(title).
                //setSound(alarmSound).
                        setAutoCancel(true);
        notificationManager.notify(100, builder.build());
        Log.e("RECEIVER", "helloooo");
    }
}
