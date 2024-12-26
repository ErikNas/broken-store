package ru.eriknas.brokenstore.mappers;

import jakarta.persistence.EntityNotFoundException;
import ru.eriknas.brokenstore.dto.store.orders.OrderCreateDTO;
import ru.eriknas.brokenstore.dto.store.orders.OrderInfoDTO;
import ru.eriknas.brokenstore.dto.store.orders.TShirtOrderDTO;
import ru.eriknas.brokenstore.entity.OrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtOrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtsEntity;
import ru.eriknas.brokenstore.repository.TShirtsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersMapper {

    public static OrderInfoDTO toDTO(OrdersEntity entity) {
        List<TShirtOrderDTO> tShirtOrders = entity.getTShirtOrders().stream()
                .map(tShirtOrderEntity -> TShirtOrderDTO.builder()
                        .tShirtId(tShirtOrderEntity.getTShirt().getId())
                        .count(tShirtOrderEntity.getCount())
                        .build())
                .collect(Collectors.toList());

        return OrderInfoDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .tShirtOrders(tShirtOrders)
                .sumOrder(entity.getSumOrder())
                .dataOrder(entity.getDataOrder())
                .dataDelivery(entity.getDataDelivery())
                .statusOrder(entity.getStatusOrder())
                .dataReturn(entity.getDataReturn())
                .reasonForReturn(entity.getReasonForReturn())
                .archivedAt(entity.getArchivedAt() != null ? entity.getArchivedAt().toLocalDate() : null)
                .createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toLocalDate() : null)
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toLocalDate() : null)
                .build();
    }

    public static OrdersEntity toEntity(OrderCreateDTO dto, TShirtsRepository tShirtRepository) {

        OrdersEntity entity = OrdersEntity.builder()
                .userId(dto.getUserId())
                .dataDelivery(dto.getDataDelivery())
                .tShirtOrders(new ArrayList<>())
                .build();

        List<TShirtOrdersEntity> tShirtOrderEntities = dto.getTShirtOrders().stream()
                .map(tShirtOrderDTO -> {
                    TShirtOrdersEntity tShirtEntity = new TShirtOrdersEntity();

                    TShirtsEntity tShirt = tShirtRepository.findById(tShirtOrderDTO.getTShirtId())
                            .orElseThrow(EntityNotFoundException::new);

                    tShirtEntity.setTShirt(tShirt);
                    tShirtEntity.setCount(tShirtOrderDTO.getCount());
                    tShirtEntity.setOrder(entity);

                    return tShirtEntity;
                })
                .collect(Collectors.toList());

        entity.setTShirtOrders(tShirtOrderEntities);
        return entity;
    }
}