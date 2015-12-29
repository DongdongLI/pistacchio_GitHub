package com.clover.example.readcurrentorderexample.pojo;

/**
 * Created by lidongdong on 10/26/15.
 */
public class Item {
    private String associated_items;
    private String extra;
    private String item_for;
    private String item_id;
    private Double item_price;
    private Integer item_qty;
    private String item_title;
    private Double item_total_price;
    private String pos_id;
    private String special_notes;

    public String getAssociated_items() {
        return associated_items;
    }

    public void setAssociated_items(String associated_items) {
        this.associated_items = associated_items;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getItem_for() {
        return item_for;
    }

    public void setItem_for(String item_for) {
        this.item_for = item_for;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public Double getItem_price() {
        return item_price;
    }

    public void setItem_price(Double item_price) {
        this.item_price = item_price;
    }

    public Integer getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(Integer item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public Double getItem_total_price() {
        return item_total_price;
    }

    public void setItem_total_price(Double item_total_price) {
        this.item_total_price = item_total_price;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getSpecial_notes() {
        return special_notes;
    }

    public void setSpecial_notes(String special_notes) {
        this.special_notes = special_notes;
    }

    @Override
    public String toString() {
        return getItem_id()+": "+getItem_title()+", "+getItem_total_price();
    }
}
