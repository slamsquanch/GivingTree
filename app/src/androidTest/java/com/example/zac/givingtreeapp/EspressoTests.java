package com.example.zac.givingtreeapp;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.zac.givingtreeapp.activity.MainActivity;
import com.example.zac.givingtreeapp.classes.Day;
import com.example.zac.givingtreeapp.database.DBHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Created by Zac on 2017-12-05.
 */

@RunWith(AndroidJUnit4.class)
public class EspressoTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    public MainActivity activity;

    DBHelper dbHelper;

    // Fill the empty SQLite DB with "acts of kindeness messages" for each day of Christmas advent
    private void populateDB(String msg, int count) {

        for (int i = 0; i < count; i++) {
            activity.storeMessage(i, msg);
        }
        activity.storeMessage(count, "buy the person behind you in line their coffee");
    }


    // empty the local SQLite DB before we populate with test data
    private void emptyDB() {
        ArrayList<Day> daylist = dbHelper.getCalendar();
        for (int i = 0; i < daylist.size(); i++) {
            dbHelper.deleteMessage(daylist.get(i).getDay());
        }
    }

    @Before
    public void before() {
        activity = mActivityRule.getActivity();
        dbHelper = DBHelper.getInstance(InstrumentationRegistry.getTargetContext());
        emptyDB();            // empty SQLite Database
        populateDB("joe", 3);  // populate with 3 days that have the message "Joe"
    }

    @After
    public void after() {
        emptyDB();            // empty SQLite Database
        dbHelper.close();
    }


    /* TEST 1:
    *    Outside-notfication interaction with local SQLite Database:
    *    We are pretending to be the received Firebase push notification with a message in it.
    *    We are checking to see if the local database for Day 2 returns the same message that
    *    we (notification) added to it.
    */
    @Test
    public void storageReceivedNotification() throws Exception {
        Log.e("MSG", String.valueOf(dbHelper.getMessage(2)));
        assertEquals("joe", dbHelper.getMessage(2).getMessage());
    }




    /* Test 2:
    *    Test the pop-up dialog message and Date title that appear when a button is clicked
    *    and dialog is opened
    */
    @Test
    public void correct_message_insideDialog() throws Exception {
        Log.e("MSG", String.valueOf(dbHelper.getMessage(3)));
        for (int i = 0; i < 4; i ++)
        pressBack(); // exit startup dialog
        onView(withId(R.id.button4)).perform(click());  // click on Day 4 button
        onView(withText("Dec. 4")).check(matches(isDisplayed()));  // compare the title of the pop-up dialog
        assertEquals("buy the person behind you in line their coffee", dbHelper.getMessage(4).getMessage());
        pressBack(); //exit the dialog
        //assertEquals("buy the person behind you in line their coffee", dbHelper.getMessage(2).getMessage());
    }


    /* Test 3:
    *    Assert equals the original "day 4" message that's stored inside Day 4 and shown on the pop-up dialog
    *    Then open the swap message dialog and hit "swap" to retrieve a new random message from extraMessages[] array.
    *    I set the extraMessagesArray to only be 1 message, because the message will get picked randomly and I want
    *    to assert equals the newly replaced Day 4 message with the extra message that I stored inside extraMessages[]
    *    Also checks that the dialogs have the correct title corresponding to the date.
    */
    @Test
    public void check_swap_message_inside_dialog() throws Exception {
        String extra_msg_to_swap[] = {"don't be on Santa's naughty list"};
        onView(withText("Dec. 4")).check(matches(isDisplayed()));  // compare the title of the pop-up dialog
        assertEquals("buy the person behind you in line their coffee", dbHelper.getMessage(4).getMessage());
        for (int i = 0; i < 4; i ++)
            pressBack(); // exit "message received" dialogs for 4 messages
        activity.setExtraMessages(extra_msg_to_swap);
        activity.swapMessage(4);
        activity.swapDialog("buy the person behind you in line their coffee", 4);
        pressBack();
        assertEquals("don't be on Santa's naughty list", dbHelper.getMessage(4).getMessage());
        onView(withId(R.id.button4)).perform(click());  // click on Day 4 button
        onView(withText("Dec. 4")).check(matches(isDisplayed()));  // compare the title of the pop-up dialog
        pressBack(); //exit the dialog
    }
}
