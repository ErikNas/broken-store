package ru.eriknas.brokenstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eriknas.brokenstore.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order order);
}
