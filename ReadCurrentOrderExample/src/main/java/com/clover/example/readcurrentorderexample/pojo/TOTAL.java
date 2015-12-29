package com.clover.example.readcurrentorderexample.pojo;

/**
 * Created by lidongdong on 10/26/15.
 */
public class TOTAL {
    private Double PAYMENT;
    private Double coupon_discount;
    private Double delivery_charges;
    private Double driver_charge;
    private Double sub_total;
    private Double tax;
    private Double tip_amt;
    private Double total;

    public Double getPAYMENT() {
        return PAYMENT;
    }

    public void setPAYMENT(Double PAYMENT) {
        this.PAYMENT = PAYMENT;
    }

    public Double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(Double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public Double getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(Double delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public Double getDriver_charge() {
        return driver_charge;
    }

    public void setDriver_charge(Double driver_charge) {
        this.driver_charge = driver_charge;
    }

    public Double getSub_total() {
        return sub_total;
    }

    public void setSub_total(Double sub_total) {
        this.sub_total = sub_total;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getTip_amt() {
        return tip_amt;
    }

    public void setTip_amt(Double tip_amt) {
        this.tip_amt = tip_amt;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
