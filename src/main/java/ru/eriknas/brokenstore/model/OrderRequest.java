package ru.eriknas.brokenstore.model;

public class OrderRequest {

    private Long tShirtId;
    private String size;
    private String color;
    private Integer quantity;
    public Order orderInfo;


    public OrderRequest(Long tShirtId, String size, String color, Integer quantity, Order orderInfo) {
        this.tShirtId = tShirtId;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.orderInfo = orderInfo;
    }

    public Long getTShirtId() {
        return tShirtId;
    }

    public void setTShirtId(Long tShirtId) {
        this.tShirtId = tShirtId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Order getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(Order orderInfo) {
        this.orderInfo = orderInfo;
    }
}
