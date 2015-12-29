package com.clover.example.readcurrentorderexample.event;

/**
 * Created by lidon on 11/15/2015.
 */
public class OrdersAvailableEvent {
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public OrdersAvailableEvent(String data) {
        this.data = data;
    }
}
