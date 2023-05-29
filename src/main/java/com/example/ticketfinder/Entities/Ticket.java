package com.example.ticketfinder.Entities;

import java.util.Objects;

public class Ticket {

    private int id;

    private int concertId;
    private float price;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && concertId == ticket.concertId && Float.compare(ticket.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, concertId, price);
    }
}
