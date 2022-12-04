package com.rw.Model;

import java.time.LocalDate;

public class ServerFlightsResponse extends ServerResponse{
    public String Where;

    public String WhereTo;

    public LocalDate Date;
    public String Time;
    public String TimeAr;



    public ServerFlightsResponse(){}

    public ServerFlightsResponse(String where, String whereTo, LocalDate date, String Time, String TimeAr){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
        this.Time = Time;
        this.TimeAr = TimeAr;
    }
}
