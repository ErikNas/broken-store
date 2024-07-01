package ru.eriknas.brokenstore.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eriknas.brokenstore.model.TShirt;

import java.util.List;
import java.util.Optional;

@Repository
public interface TShirtRepository extends JpaRepository<TShirt, Long> {
    List<TShirt> findByColor(String color);
    List<TShirt> findByTagsIn(List<String> tags);
    Optional<TShirt> findById(Long id);
    void delete(TShirt tShirt);
    boolean existsById(Long id);
}
