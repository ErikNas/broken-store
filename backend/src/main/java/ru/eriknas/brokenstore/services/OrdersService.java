package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.eriknas.brokenstore.dto.store.orders.OrderDTO;
import ru.eriknas.brokenstore.dto.store.orders.OrderInfoDTO;
import ru.eriknas.brokenstore.dto.store.orders.TShirtOrderDTO;
import ru.eriknas.brokenstore.entity.OrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtOrdersEntity;
import ru.eriknas.brokenstore.entity.TShirtsEntity;
import ru.eriknas.brokenstore.exception.InvalidPageSizeException;
import ru.eriknas.brokenstore.exception.NotFoundException;
import ru.eriknas.brokenstore.exception.ValidationException;
import ru.eriknas.brokenstore.mappers.OrdersMapper;
import ru.eriknas.brokenstore.repository.OrdersRepository;
import ru.eriknas.brokenstore.repository.TShirtsRepository;
import ru.eriknas.brokenstore.repository.UsersRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.eriknas.brokenstore.common.Constants.*;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final TShirtsRepository tShirtsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public OrdersService(OrdersRepository repository, TShirtsRepository tShirtsRepository, UsersRepository usersRepository) {
        this.ordersRepository = repository;
        this.tShirtsRepository = tShirtsRepository;
        this.usersRepository = usersRepository;
    }

    // Метод валидации пользователя
    private void validateUserExists(int userId) {
        if (!usersRepository.existsById(userId)) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
    }

    // Метод преобразования списка заказанных футболок в сущности
    private List<TShirtOrdersEntity> createTShirtOrders(OrdersEntity order, List<TShirtOrderDTO> tShirtOrders) {
        return tShirtOrders.stream()
                .map(tShirtOrder -> {
                    Integer tShirtId = tShirtOrder.getTShirtId();
                    Integer count = tShirtOrder.getCount();

                    // Проверка на количество футболок
                    if (count == null) {
                        throw new ValidationException("Необходимо указать количество футболок");
                    }
                    if (count < 0) {
                        throw new ValidationException("Количество футболок должно быть ⩾ 0");
                    }

                    TShirtsEntity tShirtsEntity = tShirtsRepository.findById(tShirtId)
                            .orElseThrow(() -> new NotFoundException(String.format(TSHIRT_NOT_FOUND, tShirtId)));

                    TShirtOrdersEntity tShirtOrdersEntity = new TShirtOrdersEntity();
                    tShirtOrdersEntity.setOrder(order);
                    tShirtOrdersEntity.setCount(count);
                    tShirtOrdersEntity.setTShirt(tShirtsEntity);

                    return tShirtOrdersEntity;
                })
                .collect(Collectors.toList());
    }

    public OrderInfoDTO createOrder(OrderDTO dto) {
        validateUserExists(dto.getUserId());

        // Создаем сущность заказа
        OrdersEntity order = OrdersMapper.toEntity(dto, tShirtsRepository);

        List<TShirtOrdersEntity> tShirtOrders = createTShirtOrders(order, dto.getTShirtOrders());

        double totalSum = tShirtOrders.stream()
                .mapToDouble(t -> t.getTShirt().getPrice() * t.getCount())
                .sum();

        order.setStatusOrder("Создан");
        order.setSumOrder(totalSum);
        order.setDataOrder(OffsetDateTime.now().toLocalDate());
        order.setCreatedAt(OffsetDateTime.now());

        return OrdersMapper.toDTO(ordersRepository.save(order));
    }

    public OrderInfoDTO updateOrder(String id, OrderDTO dto) {
        validateUserExists(dto.getUserId());

        // Ищем заказ по id
        OrdersEntity order = findOrderById(parseId(id));

        List<TShirtOrdersEntity> tShirtOrders = createTShirtOrders(order, dto.getTShirtOrders());

        double totalSum = tShirtOrders.stream()
                .mapToDouble(t -> t.getTShirt().getPrice() * t.getCount())
                .sum();

        // Обновляем поля сущности заказа
        order.getTShirtOrders().clear();
        order.getTShirtOrders().addAll(tShirtOrders);
        order.setStatusOrder("Обновлен");
        order.setSumOrder(totalSum);
        order.setDataDelivery(dto.getDataDelivery());
        order.setUpdatedAt(OffsetDateTime.now());

        return OrdersMapper.toDTO(ordersRepository.save(order));
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