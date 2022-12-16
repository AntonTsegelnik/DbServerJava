package com.rw.Model;

public class Booking extends ClientRequest {
    private int TicketCode;

    public Booking(int ticketCode) {
        TicketCode = ticketCode;
    }
    public Booking() {

    }

    public int getTicketCode() {
        return TicketCode;
    }

    public void setTicketCode(int ticketCode) {
        TicketCode = ticketCode;
    }
}
