package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.orders.OrderCreateDTO;
import ru.eriknas.brokenstore.dto.store.orders.OrderInfoDTO;
import ru.eriknas.brokenstore.dto.store.orders.TShirtOrderDTO;
import ru.eriknas.brokenstore.entity.OrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtsEntity;
import ru.eriknas.brokenstore.exception.InvalidPageSizeException;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.exception.ValidationException;
import ru.eriknas.brokenstore.mappers.OrdersMapper;
import ru.eriknas.brokenstore.repository.OrdersRepository;
import ru.eriknas.brokenstore.repository.TShirtsRepository;
import ru.eriknas.brokenstore.repository.UsersRepository;

import java.time.OffsetDateTime;

import static ru.eriknas.brokenstore.common.Constants.*;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final TShirtsRepository tShirtsRepository;
    private final UsersRepository usersRepository;

    private static final String ORDER_NOT_FOUND = "Заказ с id=%s не найден";
    private static final String TSHIRT_NOT_FOUND = "Футболка с id=%s не найдена";
    private static final String USER_NOT_FOUND = "Пользователь с id=%s не найден";

    @Autowired
    public OrdersService(OrdersRepository repository, TShirtsRepository tShirtsRepository, UsersRepository usersRepository) {
        this.ordersRepository = repository;
        this.tShirtsRepository = tShirtsRepository;
        this.usersRepository = usersRepository;
    }

    public OrderInfoDTO createOrder(OrderCreateDTO dto) {
        if (!usersRepository.existsById(dto.getUserId())) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, dto.getUserId()));
        }

        double totalSum = 0;

        for (TShirtOrderDTO tShirtOrder : dto.getTShirtOrders()) {
            Integer tShirtId = tShirtOrder.getTShirtId();
            if (!tShirtsRepository.existsById(tShirtId)) {
                throw new NotFoundException(String.format(TSHIRT_NOT_FOUND, tShirtId));
            }

            TShirtsEntity tShirtsEntity = tShirtsRepository.findById(tShirtId)
                    .orElseThrow(() -> new NotFoundException(String.format(TSHIRT_NOT_FOUND, tShirtId)));

            totalSum += tShirtsEntity.getPrice() * tShirtOrder.getCount();
        }

        var orderEntity = OrdersMapper.toEntity(dto, tShirtsRepository);
        var nowDate = OffsetDateTime.now();

        orderEntity.setStatusOrder("Создан");
        orderEntity.setSumOrder(totalSum);
        orderEntity.setCreatedAt(nowDate);
        orderEntity.setDataOrder(nowDate.toLocalDate());

        ordersRepository.save(orderEntity);

        return OrdersMapper.toDTO(orderEntity);
    }

    public OrdersEntity getOrderById(String id) {
        return findOrderById(parseId(id));
    }

    public void deleteOrder(String id) {
        int idInt = parseId(id);
        findOrderById(idInt);
        ordersRepository.deleteById(idInt);
    }

    public Page<OrdersEntity> getAllOrders(int page, int size) {

        if (page < 0) {
            throw new InvalidPageSizeException(INVALID_PAGE);
        }
        if (size <= 0) {
            throw new InvalidPageSizeException(INVALID_SIZE);
        }

        return ordersRepository.findAll(PageRequest.of(page, size));
    }

    private int parseId(String id) {

        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            throw new ValidationException(ID_MUST_BE_INTEGER);
        }
    }

    private OrdersEntity findOrderById(int id) {

        return ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ORDER_NOT_FOUND, id)));
    }
}