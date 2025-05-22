package pharmacy.pharmacy.dto;

import pharmacy.pharmacy.entity.OrderItem;

import java.util.UUID;

public class OrderItemDTO {
    private UUID id;
    private String productName;
    private int quantity;
    private double price;

    // Constructor to map OrderItem to DTO
    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
