package com.rw.Model;

import java.time.LocalDate;

public class ServerFlightsResponse extends ServerResponse{
    public String Where;

    public String WhereTo;

    public LocalDate Date;

    public ServerFlightsResponse(){}

    public ServerFlightsResponse(String where, String whereTo, LocalDate date){
        this.Where = where;
        this.WhereTo = whereTo;
        this.Date = date;
    }
}
