package ru.eriknas.brokenstore.model;

import jakarta.persistence.Entity;

@Entity
public class Order {
    private Long tShirtId;
    private Integer quantity;
    private String customerName;
    private String shippingAddress;
    private Double totalCost;


    public Long getTShirtId() {
        return tShirtId;
    }

    public void setTShirtId(Long tShirtId) {
        this.tShirtId = tShirtId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Long gettShirtId() {
        return tShirtId;
    }

    public void settShirtId(Long tShirtId) {
        this.tShirtId = tShirtId;
    }
}
