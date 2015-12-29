package com.clover.example.readcurrentorderexample.pojo;

/**
 * Created by lidongdong on 10/25/15.
 */
public class Order {
    private String order_id;

    // Call Info
    private CALLINFO callInfo;

    //Fax info: nothing important
    private FAXINFO faxInfo;
    // Order Info
    private ORDERINFO orderInfo;

    // ORDERS
    private ORDERS orders;

    //TOTAL
    private TOTAL total;
    //web-app
    private String app_id;
    private String app_key;
    private String auth_id;

    /*********************************getters and setters***********************************/

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setCallInfo(CALLINFO callInfo) {
        this.callInfo = callInfo;
    }

    public CALLINFO getCallInfo() {
        return callInfo;
    }

    public FAXINFO getFaxInfo() {
        return faxInfo;
    }

    public void setFaxInfo(FAXINFO faxInfo) {
        this.faxInfo = faxInfo;
    }

    public ORDERINFO getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(ORDERINFO orderInfo) {
        this.orderInfo = orderInfo;
    }

    public ORDERS getOrders() {
        return orders;
    }

    public void setOrders(ORDERS orders) {
        this.orders = orders;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

}
