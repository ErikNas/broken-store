package ru.eriknas.brokenstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;

public interface TShirtsRepository extends CrudRepository<TShirtsEntity, Integer> {

    Page<TShirtsEntity> findAll(Pageable pageable);
}