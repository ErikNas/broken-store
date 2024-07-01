package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.model.Order;
import ru.eriknas.brokenstore.model.OrderRequest;
import ru.eriknas.brokenstore.services.OrderService;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Создать заказ на футболку")
    @ApiResponse(responseCode = "200", description = "Заказ успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка в данных заказа")
    @PostMapping("/store/order")
    public Order createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @Operation(summary = "Получение заказа по номеру")
    @ApiResponse(responseCode = "200", description = "Заказ найден")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @GetMapping("/store/order/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return orderService.findOrderById(orderId)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удаление заказа по номеру")
    @ApiResponse(responseCode = "200", description = "Заказ успешно удален")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @DeleteMapping("/store/order/{orderId}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long orderId) {
        if (orderService.deleteOrderById(orderId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
