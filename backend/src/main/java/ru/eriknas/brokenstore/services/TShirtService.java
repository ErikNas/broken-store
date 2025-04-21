package ru.eriknas.brokenstore.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.TShirtsDTO;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;
import ru.eriknas.brokenstore.repository.TShirtRepository;

@Service
public class TShirtService {

    private final TShirtRepository tShirtsRepository;

    private static final String TSHIRT_NOT_FOUND = "Футболка с id=%s не найдена";

    @Autowired
    public TShirtService(TShirtRepository repository) {
        this.tShirtsRepository = repository;
    }

    public TShirtsEntity createTShirt(TShirtsDTO dto) {
        return tShirtsRepository.save(TShirtsMapper.toEntity(dto));
    }

    public TShirtsEntity updateTShirt(Integer id, @Valid TShirtsDTO dto) {
        TShirtsEntity entity = tShirtsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(TSHIRT_NOT_FOUND, id)));

        entity.setArticle(dto.getArticle());
        entity.setName(dto.getName());
        entity.setSize(dto.getSize());
        entity.setColor(dto.getColor());
        entity.setImage(dto.getImage());
        entity.setMaterial(dto.getMaterial());
        entity.setCountryOfProduction(dto.getCountryOfProduction());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());

        return tShirtsRepository.save(entity);
    }

    public void deleteTShirt(Integer id) {
        tShirtsRepository.deleteById(id);
    }

    public TShirtsEntity getTShirtById(Integer id) {
        return tShirtsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(TSHIRT_NOT_FOUND, id)));
    }

    public Page<TShirtsEntity> getAllTShirts(int page, int size) {
        return tShirtsRepository.findAll(PageRequest.of(page, size));
    }
}