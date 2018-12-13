package org.sokol.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Trip {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String destination;
    private double price;


    @ManyToMany(mappedBy = "trips")
    private Set<Customer> customers = new HashSet<>();


    public Trip(String destination, double price) {
        this.destination = destination;
        this.price = price;
    }


    public Trip() {
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



}