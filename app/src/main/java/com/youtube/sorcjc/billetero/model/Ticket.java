package com.youtube.sorcjc.billetero.model;

public class Ticket {

    private int ticketNumber;
    private int totalSold;
    private int surplus;

    public Ticket(int ticketNumber, int totalSold, int surplus) {
        this.ticketNumber = ticketNumber;
        this.totalSold = totalSold;
        this.surplus = surplus;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }
}
