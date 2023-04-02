package com.example.westillshare.model;

import java.time.LocalDate;
import java.util.Date;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private Date date;
    private String personWhoPosted;



    public Expense(int id, String description, double amount, Date date, String personWhoPosted) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.personWhoPosted = personWhoPosted;
    }

    public Expense() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPersonWhoPosted() {
        return personWhoPosted;
    }

    public void setPersonWhoPosted(String personWhoPosted) {
        this.personWhoPosted = personWhoPosted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", personWhoPosted='" + personWhoPosted + '\'' +
                '}';
    }
}


