package com.rw.Model;



import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class FlightsRequest extends ClientRequest {
    public String Where;

    public String WhereTo;

    public LocalDate Date;


    public FlightsRequest(){}
    public FlightsRequest(String where, String whereTo, LocalDate date){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
    }

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
