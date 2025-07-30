package ru.eriknas.brokenstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.eriknas.brokenstore.models.entities.UsersEntity;

@Repository
public interface UsersRepository extends CrudRepository<UsersEntity, Integer> {
    Page<UsersEntity> findAll(Pageable pageable);
    boolean existsByEmail(String email);
}
