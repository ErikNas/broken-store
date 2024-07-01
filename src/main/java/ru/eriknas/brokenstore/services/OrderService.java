package ru.eriknas.brokenstore.services;

import org.springframework.beans.factory.annotation.Autowired;

import ru.eriknas.brokenstore.model.Order;
import ru.eriknas.brokenstore.model.OrderRequest;
import ru.eriknas.brokenstore.model.TShirt;

import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepository;
    @Autowired
    private TShirtRepository tShirtRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Order createOrder(OrderRequest orderRequest) {
        // Валидация данных заказа
        validateOrderRequest(orderRequest);

        // Создание объекта заказа
        Order order = new Order();
        order.setTShirtId(orderRequest.getTShirtId());
        order.setQuantity(orderRequest.getQuantity());
        order.setCustomerName(orderRequest.getOrderInfo().getCustomerName());
        order.setShippingAddress(orderRequest.getOrderInfo().getShippingAddress());

        // Расчет стоимости
//        double cost = calculateCost(orderRequest);
//        order.setTotalCost(cost);

        // Сохранение заказа в базе данных
        Order savedOrder = orderRepository.save(order);

        // Отправка подтверждения заказа
        sendOrderConfirmation(savedOrder);

        return savedOrder;
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        // Проверка наличия футболки в каталоге
        if (!tShirtRepository.existsById(orderRequest.getTShirtId())) {
            throw new IllegalArgumentException("Футболка с данным ID не найдена в каталоге.");
        }

        // Проверка доступности нужного размера и цвета
        TShirt tShirt = tShirtRepository.findById(orderRequest.getTShirtId()).orElseThrow();
        if (!tShirt.getColor().equals(orderRequest.getColor()) || !tShirt.getSize().equals(orderRequest.getSize())) {
            throw new IllegalArgumentException("Указанный размер или цвет футболки недоступен.");
        }
    }

//    private double calculateCost(OrderRequest orderRequest) {
//         Реализовать расчет стоимости заказа
//    }

    private void sendOrderConfirmation(Order order) {
        // Реализовать отправку подтверждения заказа
    }

    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public boolean deleteOrderById(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        } else {
            return false;
        }
    }
}
