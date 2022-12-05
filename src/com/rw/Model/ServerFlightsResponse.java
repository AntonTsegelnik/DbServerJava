package com.rw.Model;

import java.time.LocalDate;

public class ServerFlightsResponse extends ServerResponse{
    private String FlightCode;
    private String Where;

    private String WhereTo;

    private LocalDate Date;
    private String Time;

    private String TimeAr;
    private int Coupe;
    private int Res;
    private int Seats;




    public ServerFlightsResponse(){}

    public ServerFlightsResponse(String where, String whereTo, LocalDate date, String time, String TimeAr, String flightCode, int coupe, int reser, int seats){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
        this.Time = time;
        this.TimeAr = TimeAr;
        this.FlightCode = flightCode;
        this.Coupe = coupe;
        this.Res = reser;
        this.Seats = seats;
    }

    public String getFlightCode() {
        return FlightCode;
    }

    public void setFlightCode(String flightCode) {
        FlightCode = flightCode;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
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


    public int getCoupe() {
        return Coupe;
    }

    public void setCoupe(int coupe) {
        Coupe = coupe;
    }

    public int getRes() {
        return Res;
    }

    public void setRes(int res) {
        Res = res;
    }

    public int getSeats() {
        return Seats;
    }

    public void setSeats(int seats) {
        Seats = seats;
    }
}
