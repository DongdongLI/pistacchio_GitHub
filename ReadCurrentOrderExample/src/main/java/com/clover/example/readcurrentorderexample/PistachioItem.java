package com.clover.example.readcurrentorderexample;

/**
 * Created by lidongdong on 10/27/15.
 */
public class PistachioItem {

    private String title;
    private Integer quantity;
    private Double price;
    private Double total;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return new StringBuilder("PistachioItem{")
                .append("title='").append(title).append('\'')
                .append(", quantity=").append(quantity)
                .append(", totol=").append(total)
                .append('}').toString();
    }
}
