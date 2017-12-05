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
            "Talk to the shy person are work or school",
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


    private String message = "null";
    private String title = "null";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e("Receiver", "Broadcast received: " + action);

        if (action != null) {
            if (action.equals("my.action.string")) {
                title = intent.getExtras().getString("TITLE");
                message = intent.getExtras().getString("MSG");

                Log.e("Receiver", "broadcast received!!");
                Log.e("Receiver", message);
            }
        }
        else Log.e("Receiver", "action is null");




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
    }
}
