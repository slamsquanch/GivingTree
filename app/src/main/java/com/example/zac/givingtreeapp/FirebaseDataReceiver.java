package com.example.zac.givingtreeapp;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Zac on 2017-12-02.
 */

/**
 * This is called whenever app receives notification
 * in background/foreground state so you can
 * apply logic for background task, but still Firebase notification
 * will be shown in notification tray
 */
public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "I'm in!!!");

        String title = "";
        String body  = "";

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
            }

            title += intent.getExtras().get("gcm.notification.title");
            body  += intent.getExtras().get("gcm.notification.body");
            if (context != null)
                Toast.makeText(context, title + "\n" + body, Toast.LENGTH_LONG).show();
        }
    }
}