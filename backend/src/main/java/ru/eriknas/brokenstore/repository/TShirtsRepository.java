package ru.eriknas.brokenstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.eriknas.brokenstore.entity.TShirtsEntity;

public interface TShirtsRepository extends CrudRepository<TShirtsEntity, Integer> {
    boolean existsById(Integer id);

    Page<TShirtsEntity> findAll(Pageable pageable);
}