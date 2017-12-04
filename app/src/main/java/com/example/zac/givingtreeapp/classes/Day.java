package com.example.zac.givingtreeapp.classes;

/**
 * Created by Zac on 2017-11-28.
 */

public class Day {
    private String message1, message2, message3;
    private String sendStatus, openStatus;

    public Day(String message1, String message2, String message3) {
        this.message1 = message1;
        this.message2 = message2;
        this.message3 = message3;
    }


    public Day(String message1, String message2) {
        this.message1 = message1;
        this.message2 = message2;
    }

    public Day() {}

    public void setMessage1(String msg) {
        this.message1 = msg;
    }


    public void setMessage2(String msg) {
        this.message2 = msg;
    }


    public void setMessage3(String msg) {
        this.message3 = msg;
    }

    public void setSendStatus(String status) {
        sendStatus = status;
    }

    public void setOpenStatus(String status) {
        openStatus = status;
    }

    public String getMessage1() {
        return message1;
    }

    public String getMessage2() {
        return message2;
    }

    public String getMessage3() {
        return message3;
    }
}
