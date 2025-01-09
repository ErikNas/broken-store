package ru.eriknas.brokenstore.entity;

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

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}