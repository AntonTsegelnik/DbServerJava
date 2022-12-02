package com.rw.Model;

import java.util.Date;

public class Flights {
    public String Where;

    public String WhereTo;

    public java.util.Date Date;

    public Flights (){}
    public Flights (String where, String whereTo, Date date){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
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
