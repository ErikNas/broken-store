package ru.eriknas.brokenstore.mappers;

import ru.eriknas.brokenstore.dto.store.orders.OrderDTO;
import ru.eriknas.brokenstore.dto.store.orders.OrderInfoDTO;
import ru.eriknas.brokenstore.dto.store.orders.TShirtOrderDTO;
import ru.eriknas.brokenstore.entity.OrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtOrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtsEntity;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.repository.TShirtsRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.eriknas.brokenstore.common.Constants.TSHIRT_NOT_FOUND;

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

    public static OrdersEntity toEntity(OrderDTO dto, TShirtsRepository tShirtRepository) {
        OrdersEntity entity = OrdersEntity.builder()
                .userId(dto.getUserId())
                .dataDelivery(dto.getDataDelivery())
                .build();

        List<TShirtOrdersEntity> tShirtOrderEntities = dto.getTShirtOrders().stream()
                .map(tShirtOrderDTO -> {
                    int tShirtId = tShirtOrderDTO.getTShirtId();

                    TShirtsEntity tShirt = tShirtRepository.findById(tShirtId)
                            .orElseThrow(() -> new NotFoundException(String.format(TSHIRT_NOT_FOUND, tShirtId)));

                    return TShirtOrdersEntity.builder()
                            .tShirt(tShirt)
                            .count(tShirtOrderDTO.getCount())
                            .order(entity)
                            .build();
                })
                .collect(Collectors.toList());

        entity.setTShirtOrders(tShirtOrderEntities);

        return entity;
    }
}