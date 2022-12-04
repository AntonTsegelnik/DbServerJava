package com.rw.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class FlightsRequest extends ClientRequest {
    public String Where;

    public String WhereTo;

    public LocalDate Date;
    public String Time;

    public String TimeAr;


    public FlightsRequest(){}
    public FlightsRequest(String where, String whereTo, LocalDate date, String time, String TimeAr){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
        this.Time = time;
        this.TimeAr = TimeAr;
    }

    public String getTimeAr() {
        return TimeAr;
    }

    public void setTimeAr(String timeAr) {
        TimeAr = timeAr;
    }
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public LocalDate getDate() {
        return Date;
    }
//    public DatePicker getDate() {
//        return Date;
//    }
//
//    public void setDate(DatePicker date) {
//        Date = date;
//    }

    public String getWhere() {
        return Where;
    }

    public void setWhere(String where) {
        Where = where;
    }

    public String getWhereTo() {
        return WhereTo;
    }

    public void setWhereTo(String whereTo) {
        WhereTo = whereTo;
    }
}
