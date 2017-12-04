<?php

define("NOTIFICATION_API_KEY", "AAAAcX4V4X4:APA91bF2OI4vk36cLSUjdIa9ebzhZzveTt3Ue6dYt-q3W2AL20K2sEqFvOTX5orr9ED-E2bPpE-S_OKe-DWt1bWnwHRqTkvAXgKjsYEmpTDZgR1_N18vUpaD3Z23YnW4V0bkJU5QQfGU");

$users = getUsers();
// var_dump ($users);

$messages = array(
 0 => "Give someone a hug",
 1 => "Pay it Backward: buy coffee for the person behind you in line",
 2 => "Offer to help you neighbours setup their Christmas decorations",
 3 => "Offer to shovel someone's driveway or mow their lawn.",
 4 => "Call your grandparents. Call them!",
 5 => "Pickup and throw away the litter you see when you go outside today",
 6 => "Help someone struggling with heavy bags",
 7 => "Do the dishes even if it's your roommate's turn",
 8 => "Wash someone's car.",
 9 => "Dog or catsit for free",
 10 => "Make two lunches and give one away",
 11 => "Reduce air pollution by carpooling",
 12 => "Say yes at the store when the cashier asks if you want to donate $1 to whichever cause",
 13 => "Plant a tree",
 14 => "Purchase some extra dog or cat food and drop it off at an animal shelter",
 15 => "Bring a security guard a hot cup of coffee",
 16 => "Tell your siblings how much you appreciate them",
 17 => "Give up your seat to someone (anyone!) on the bus or subway",
 18 => "Hold the door open for people today",
 19 => "Stop to talk to a homeless person",
 20 => "Donate your old eyeglasses so someone else can use them",
 21 => "Bring delicious snacks or dessert to work for everyone",
 22 => "Let someone go in front of you in line who only has a few items",
 23 => "Talk to the shy person are work or school",
 24 => "Cook a meal or do a load of laundry for a friend who just had a baby or is going through a difficult time",
 25 => "Surprise a neighbor with freshly baked cookies or treats",
 26 => "Compliment at laast three people today",
 27 => "Say hi to the person next to you on the elevator",
 28 => "Leave a gas gift card at a gas pump",
 29 => "Leave quarters at the laundromat",
 30 => "Have a LinkedIn account? Write a recommendation for coworker or connection",
 31 => "Leave unused coupons next to corresponding products in the grocery store",
 32 => "Try to make sure every person in a group conversation feels included",
 33 => "Set an alarm on your phone to go off at three different times during the day. In those moments, do something kind for someone else",
 34 => "Send a gratitude email to a coworker who deserves more recognition. Or tell them in person",
 35 => "Know parents who could use a night out? Offer to babysit for free",
 36 => "Return shopping carts for people at the grocery store",
 37 => "Have a clean up party at a beach or park",
 38 => "Everyone is important. Learn the names of your office security guard, the person at the front desk and other people you see every day",
 39 => "Write your partner a list of things you love about them",
 40 => "Keep an extra umbrella at work, so you can lend it out when it rains",
 41 => "Send a ‘Thank you’ card or note to the officers at your local police or fire station",
 42 => "Run an errand for a family member who is busy",
 43 => "Leave a box of goodies in your mailbox for your mail carrier",
 44 => "Tape coins around a playground for kids to find",
 45 => "Put your phone away while in the company of others",
 46 => "When you hear that discouraging voice in your head, tell yourself something positive — you deserve kindness too!",
 47 => "Go through your closet and donate clothes that you no longer need to charity",
 48 => "Say something positive to 5 people on social media today",
 49 => "Offer to walk your neighbors dog",
 50 => "Offer to cook a meal",
);


$index = rand(0, count($messages) - 1);


foreach($users AS $token) {
    echo "notifying to $token\n";

    $days = getDaysLeft();

    var_dump ($days);

    notify2($token,
        "Only $days Days 'til Christmas!",
        "{$messages[$index]}", $index);

}


function getDaysLeft() {

    $timestamp = time();
    $date = getdate($timestamp);
    $dayOfMonth = $date["mday"];
    $month = $date["mon"];
    
    if ($month == 12 && $dayOfMonth < 25) {
        return (25 - $dayOfMonth);    // days 'til Xmas.
    }
    return 0;
}


function getUsers() {
    $url = "https://givingtreeapp-470fc.firebaseio.com/tokens.json";

    //if(DEBUG) echo "downloading $url\n";
//    $ch = curl_init($url);
//    curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
//    curl_setopt($ch, CURLOPT_POST, 1);
//    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: text/plain'));
    $json = curl_exec($ch);

    curl_close($ch);
    return json_decode($json, true);

}

function notify2($token, $title, $body, $id,  $keyValues=[]) {
    $ch = curl_init("https://fcm.googleapis.com/fcm/send");

    //The device token.
    //$token = "DEVICE_TOKEN_HERE"; //token here

    $data = array (
        'msg'     => $body,
        'msgID'   => $id
    );
    //Creating the notification array.
    $notification = array (
        'title' => $title,
        'text' => $body,
        'sound' => 'default',
        'icon' => 'ic_star_icon',
        'badge' => '1'
        );   


    $notification = array_merge($keyValues, $notification);


    //This array contains, the token and the notification. The 'to' attribute stores the token.
    $arrayToSend = array('to' => $token, 'notification' => $notification,'priority'=>'high', 'data' => $data);

    //Generating JSON encoded string form the above array.
    $json = json_encode($arrayToSend);
    //Setup headers:
    $headers = array();
    $headers[] = 'Content-Type: application/json';
    $headers[] = 'Authorization: key=' . NOTIFICATION_API_KEY; // key here

    //Setup curl, add headers and post parameters.
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);

    //Send the request
    $response = curl_exec($ch);

    //Close request
    curl_close($ch);
    
    return $response;

}
?>