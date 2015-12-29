package com.clover.example.readcurrentorderexample.pojo;

import java.util.Map;

/**
 * Created by lidongdong on 10/26/15.
 */
public class Root {
    private String status;
    private Map<String,Order> orders;
//    private JSONObject orders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, Order> orders) {
        this.orders = orders;
    }
}
