package com.zalologin.model;

import com.zalologin.Week;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/29/2017.
 */

public class Date {
    private int year;
    private int month;
    private int day;
    private @Week String week;

    public Date(int year, int month, int day, String week) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getWeek() {
        return week;
    }
}
