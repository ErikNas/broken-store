package ru.eriknas.brokenstore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TShirtOrdersEntity> tShirtOrders;

    @Column(name = "sum_order")
    private Double sumOrder;

    @Column(name = "data_order")
    private LocalDate dataOrder;

    @Column(name = "data_delivery")
    private LocalDate dataDelivery;

    @Column(name = "status_order")
    private String statusOrder;

    @Column(name = "data_return")
    private OffsetDateTime dataReturn;

    @Column(name = "reason_for_return")
    private String reasonForReturn;

    @Column(name = "archived_at")
    private OffsetDateTime archivedAt;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @JsonProperty("archived_at")
    public OffsetDateTime getArchivedAt() {
        return archivedAt;
    }

    @JsonProperty("created_at")
    public OffsetDateTime getCreateAt() {
        return createdAt;
    }

    @JsonProperty("updated_at")
    public OffsetDateTime getUpdateAt() {
        return updatedAt;
    }
}