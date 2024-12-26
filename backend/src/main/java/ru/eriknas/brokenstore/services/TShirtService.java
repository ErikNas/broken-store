package ru.eriknas.brokenstore.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtCreateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtUpdateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtsInfoDTO;
import ru.eriknas.brokenstore.entity.TShirtsEntity;
import ru.eriknas.brokenstore.exception.InvalidPageSizeException;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.exception.ValidationException;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.repository.TShirtsRepository;

import java.time.OffsetDateTime;

import static ru.eriknas.brokenstore.common.Constants.*;

@Service
public class TShirtService {

    private final TShirtsRepository tShirtsRepository;

    private static final String TSHIRT_NOT_FOUND = "Футболка с id=%s не найдена";

    @Autowired
    public TShirtService(TShirtsRepository repository) {
        this.tShirtsRepository = repository;
    }

    public TShirtsInfoDTO createTShirt(@Valid TShirtCreateDTO dto) {
        TShirtsEntity entity = TShirtsMapper.toEntity(dto);
        entity.setCreatedAt(OffsetDateTime.now());
        return TShirtsMapper.toDto(tShirtsRepository.save(entity));
    }

    public TShirtsEntity updateTShirt(String id, TShirtUpdateDTO dto) {
        int idInt = parseId(id);
        TShirtsEntity entity = findTShirtById(idInt);
        updateEntity(entity, dto);
        entity.setUpdatedAt(OffsetDateTime.now());
        return tShirtsRepository.save(entity);
    }

    public void deleteTShirt(String id) {
        int idInt = parseId(id);
        findTShirtById(idInt);
        try {
            tShirtsRepository.deleteById(idInt);
        } catch (DataIntegrityViolationException ex) {
            throw new ValidationException("Невозможно удалить футболку, так как она используется в заказе");
        }
    }

    public TShirtsEntity getTShirtById(String id) {
        return findTShirtById(parseId(id));
    }

    public Page<TShirtsEntity> getAllTShirts(int page, int size) {

        if (page < 0) {
            throw new InvalidPageSizeException(INVALID_PAGE);
        }
        if (size <= 0) {
            throw new InvalidPageSizeException(INVALID_SIZE);
        }

        return tShirtsRepository.findAll(PageRequest.of(page, size));
    }

    private int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            throw new ValidationException(ID_MUST_BE_INTEGER);
        }
    }

    private TShirtsEntity findTShirtById(int id) {
        return tShirtsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(TSHIRT_NOT_FOUND, id)));
    }

    private void updateEntity(TShirtsEntity entity, TShirtUpdateDTO dto) {
        entity.setArticle(dto.getArticle());
        entity.setName(dto.getName());
        entity.setSize(dto.getSize());
        entity.setColor(dto.getColor());
        entity.setImage(dto.getImage());
        entity.setMaterial(dto.getMaterial());
        entity.setCountryOfProduction(dto.getCountryOfProduction());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
    }
}