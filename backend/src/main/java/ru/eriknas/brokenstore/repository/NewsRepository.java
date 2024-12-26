package ru.eriknas.brokenstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.eriknas.brokenstore.entity.NewsEntity;

@Repository
public interface NewsRepository extends CrudRepository<NewsEntity, Integer> {

    Page<NewsEntity> findAll(Pageable pageable);

}
