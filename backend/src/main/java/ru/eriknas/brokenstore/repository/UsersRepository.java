package ru.eriknas.brokenstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.eriknas.brokenstore.entity.UsersEntity;

@Repository
public interface UsersRepository extends CrudRepository<UsersEntity, Integer> {
    boolean existsById(Integer id);

    Page<UsersEntity> findAll(Pageable pageable);
}
