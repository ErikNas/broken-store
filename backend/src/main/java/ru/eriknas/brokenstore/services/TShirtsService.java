package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.TShirtsDTO;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;
import ru.eriknas.brokenstore.repository.TShirtsRepository;

@Service
public class TShirtsService {

    private final TShirtsRepository tShirtsRepository;

    private static final String TSHIRT_NOT_FOUND = "Футболка с id=%s не найдена";

    @Autowired
    public TShirtsService(TShirtsRepository repository) {
        this.tShirtsRepository = repository;
    }

    public TShirtsEntity createTShirt(TShirtsDTO dto) {
        return tShirtsRepository.save(TShirtsMapper.toEntity(dto));
    }

    public TShirtsEntity updateTShirt(Integer id, TShirtsDTO dto) {
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

    public boolean existsById(Integer id) {
        return tShirtsRepository.findById(id).isEmpty();
    }

    public Page<TShirtsEntity> getAllTShirts(int page, int size) {
        return tShirtsRepository.findAll(PageRequest.of(page, size));
    }
}