package com.clover.example.readcurrentorderexample.event;

/**
 * Created by lidon on 11/15/2015.
 */
public class OrdersFetchCompleteEvent {
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public OrdersFetchCompleteEvent(String data) {
        this.data = data;
    }
}
