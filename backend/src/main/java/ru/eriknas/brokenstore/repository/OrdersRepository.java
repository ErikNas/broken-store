package ru.eriknas.brokenstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.eriknas.brokenstore.entity.OrdersEntity;

public interface OrdersRepository extends CrudRepository<OrdersEntity, Integer> {

    Page<OrdersEntity> findAll(Pageable pageable);
}
