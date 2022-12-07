package com.rw.Model;

public class Ticket extends ClientRequest {
    private int TicketCode;
    private int TrainCar;
    private int SeatNum;
    private String SeatType;
    private int PassId;
    private String  FlightCode;

    public Ticket(){}
    public Ticket(int trainCar, int seatNum, String seatType, int passId, String flightCode) {
        TrainCar = trainCar;
        SeatNum = seatNum;
        SeatType = seatType;
        PassId = passId;
        FlightCode = flightCode;
    }

    public int getTicketCode() {
        return TicketCode;
    }

    public void setTicketCode(int ticketCode) {
        TicketCode = ticketCode;
    }

    public int getTrainCar() {
        return TrainCar;
    }

    public void setTrainCar(int trainCar) {
        TrainCar = trainCar;
    }

    public int getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(int seatNum) {
        SeatNum = seatNum;
    }

    public String getSeatType() {
        return SeatType;
    }

    public void setSeatType(String seatType) {
        SeatType = seatType;
    }

    public int getPassId() {
        return PassId;
    }

    public void setPassId(int passId) {
        PassId = passId;
    }

    public String getFlightCode() {
        return FlightCode;
    }

    public void setFlightCode(String flightCode) {
        FlightCode = flightCode;
    }
}
