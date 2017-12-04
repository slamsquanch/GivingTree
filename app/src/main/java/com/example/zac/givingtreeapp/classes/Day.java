package com.example.zac.givingtreeapp.classes;

/**
 * Created by Zac on 2017-11-28.
 */

public class Day {
    private String message;
    private int day, id;

    /*public Day(String message) {
        this.message = message;
    }


    public Day(String message, int day) {
        this.message = message;
        this.day = day;
    }*/

    public Day(int id, String msg, int day) {
        this.id = id;
        this.message = msg;
        this.day = day;
    }


    public Day() {}

    public void setMessage(String msg) {
        message = msg;
    }

    public void setDay(int d) {
        day = d;
    }

    public void setID (int ID) {
        id = ID;
    }

    public String getMessage() {
        return message;
    }

    public int getDay() {
        return day;
    }

    public int getID() {
        return id;
    }
}
