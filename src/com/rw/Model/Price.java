package com.rw.Model;

public class Price extends ClientRequest {
    private String FlightCode;
    private String SeatType;
    private double Price;


    public Price(){}
    public Price(String flightCode, String seatType, double price) {
        FlightCode = flightCode;
        SeatType = seatType;
        Price = price;
    }

    public String getFlightCode() {
        return FlightCode;
    }

    public void setFlightCode(String flightCode) {
        FlightCode = flightCode;
    }

    public String getSeatType() {
        return SeatType;
    }

    public void setSeatType(String seatType) {
        SeatType = seatType;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
