package com.rw.Model;



import java.io.Serializable;
import java.util.Date;

public class FlightsRequest implements Serializable {
    public String Where;

    public String WhereTo;

    public java.util.Date Date;
    public String requestType;

    public FlightsRequest(){}
    public FlightsRequest(String where, String whereTo, Date date){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
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
