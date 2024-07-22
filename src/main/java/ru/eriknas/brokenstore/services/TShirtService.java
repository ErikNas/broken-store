package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.model.TShirt;
import ru.eriknas.brokenstore.repository.TShirtRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TShirtService {
    @Autowired
    private TShirtRepository tShirtRepository;

    public TShirt updateTShirt(TShirt tShirt) {
        Optional<TShirt> existingTShirt = tShirtRepository.findById(tShirt.getId());
        if (existingTShirt.isPresent()) {
            // Обновляем поля футболки
            TShirt updatedTShirt = existingTShirt.get();
            updatedTShirt.setBrand(tShirt.getBrand());
            updatedTShirt.setSize(tShirt.getSize());
            updatedTShirt.setColor(tShirt.getColor());
            updatedTShirt.setPrice(tShirt.getPrice());
            updatedTShirt.setGender(tShirt.getGender());
            updatedTShirt.setId(tShirt.getId());
            // Сохраняем обновление
            return tShirtRepository.save(updatedTShirt);
        } else {
            throw new RuntimeException("Футболка с ID " + tShirt.getId() + " не найдена");
        }
    }

    public TShirt updateTShirt(Long id, TShirt updatedTShirt) {
        // Получаем существующую футболку по идентификатору
        TShirt tShirt = tShirtRepository.findById(id)
                .orElse(null);
        if (tShirt == null) {
            throw new RuntimeException("Футболка с ID " + id + " не найдена");
        }
        //Обновление полей футболки
        if (updatedTShirt.getBrand()!= null) {
            tShirt.setBrand(updatedTShirt.getBrand());
        }
        if (updatedTShirt.getSize()!= null) {
            tShirt.setSize(updatedTShirt.getSize());
        }
        if (updatedTShirt.getColor() != null) {
            tShirt.setColor(updatedTShirt.getColor());
        }
        if (updatedTShirt.getGender()!= null) {
            tShirt.setGender(updatedTShirt.getGender());
        }
        if (updatedTShirt.getPrice() > 0) {
            tShirt.setPrice(updatedTShirt.getPrice());
        }
        if (updatedTShirt.getTags()!= null) {
            tShirt.setTags(updatedTShirt.getTags());
        }
        if (updatedTShirt.getId()!= null) {
            tShirt.setId(updatedTShirt.getId());
        }
        return tShirtRepository.save(tShirt);
    }

    public List<TShirt> findTShirtsByColor(String color) {
        return tShirtRepository.findByColor(color);
    }

    public List<TShirt> findTShirtsByTags(List<String> tags) {
        return tShirtRepository.findAll().stream()
                .filter(tShirt -> tShirt.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    public TShirt findById(Long id) {
        return tShirtRepository.findById(id).orElse(null);
    }


    public void deleteTShirt(Long id) {
        TShirt tShirt = tShirtRepository.findById(id)
                .orElse(null);
        tShirtRepository.delete(tShirt);
    }
}
