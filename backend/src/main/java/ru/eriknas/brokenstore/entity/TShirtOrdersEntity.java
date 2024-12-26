package ru.eriknas.brokenstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_shirt_orders")
public class TShirtOrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersEntity order;

    @ManyToOne
    @JoinColumn(name = "t_shirt_id", referencedColumnName = "id")
    private TShirtsEntity tShirt;

    @Column(name = "count")
    private Integer count;
}