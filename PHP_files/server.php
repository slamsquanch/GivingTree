<?php

define("NOTIFICATION_API_KEY", "AAAAcX4V4X4:APA91bF2OI4vk36cLSUjdIa9ebzhZzveTt3Ue6dYt-q3W2AL20K2sEqFvOTX5orr9ED-E2bPpE-S_OKe-DWt1bWnwHRqTkvAXgKjsYEmpTDZgR1_N18vUpaD3Z23YnW4V0bkJU5QQfGU");

$users = getUsers();
// var_dump ($users);

$messages = array(
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
"ry to make sure every person in a group conversation feels included",
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
);


$index = rand(0, count($messages) - 1);


foreach($users AS $token) {
    echo "notifying to $token\n";

    $days = getDaysLeft();

    var_dump ($days);

    notify2($token,
        "Only $days Days 'til Christmas!",
        "{$messages[$index]}");

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

function notify2($token, $title, $body, $keyValues=[]) {
    $ch = curl_init("https://fcm.googleapis.com/fcm/send");

    //The device token.
    //$token = "DEVICE_TOKEN_HERE"; //token here

    $data = array (
        'msg'     => $body
    );
    //Creating the notification array.
    $notification = array (
        'title' => $title,
        'text' => $body,
        'sound' => 'default',
        'icon' => 'ic_star_icon',
        'badge' => '1',
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