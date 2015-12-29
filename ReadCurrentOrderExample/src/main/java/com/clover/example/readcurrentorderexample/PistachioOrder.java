package com.clover.example.readcurrentorderexample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lidongdong on 10/27/15.
 */
public class PistachioOrder {

    private String orderId;
    private List<PistachioItem> items=new ArrayList<PistachioItem>();
    private Double totalWithTax;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PistachioItem> getItems() {
        return items;
    }

    public void setItems(List<PistachioItem> items) {
        this.items = items;
    }

    public Double getTotalWithTax() {
        return totalWithTax;
    }

    public void setTotalWithTax(Double totalWithTax) {
        this.totalWithTax = totalWithTax;
    }

    @Override
    public String toString() {
        return new StringBuilder("PistachioOrder{")
                .append("orderId='").append(orderId).append('\'')
                .append(", items=").append(items)
                .append(", totalWithTax=").append(totalWithTax)
                .append('}').toString();
    }
}
