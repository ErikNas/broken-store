package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.store.orders.OrderDTO;
import ru.eriknas.brokenstore.dto.store.orders.OrderInfoDTO;
import ru.eriknas.brokenstore.models.entities.OrdersEntity;
import ru.eriknas.brokenstore.exception.CountZeroException;
import ru.eriknas.brokenstore.mappers.OrdersMapper;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.services.OrdersService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Validated
@Tag(name = "Заказы")
public class OrderController {

    private final OrdersService ordersService;

    @Autowired
    public OrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    @Operation(summary = "Создать заказ")
    @ApiResponse(responseCode = "201 Created", description = "Заказ создан")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @SecurityRequirements
    public ResponseEntity<OrderInfoDTO> createOrder(@RequestBody @Validated OrderDTO dto) {
        dto.getTShirtOrders().forEach(tShirtOrderDTO -> {
            if (tShirtOrderDTO.getCount() <= 0) {
                throw new CountZeroException("Количество должно быть больше нуля");
            }
        });
        OrderInfoDTO orderInfo = ordersService.createOrder(dto);
        return new ResponseEntity<>(orderInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать заказ")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "404 NotFound", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<OrderInfoDTO> editOrder(@PathVariable
                                                  @Validated
                                                  @Parameter(description = "id заказа") String id,
                                                  @RequestBody @Validated OrderDTO dto) {
        OrderInfoDTO orderInfo = ordersService.updateOrder(id, dto);
        return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ")
    @ApiResponse(responseCode = "204 No Content", description = "Заказ удален")
    @ApiResponse(responseCode = "404 NotFound", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<Void> deleteOrder(@PathVariable
                                            @Validated
                                            @Parameter(description = "id заказа") String id) {
        ordersService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти заказ по id")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "404 NotFound", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<OrderInfoDTO> getOrderById(@PathVariable
                                                     @Validated
                                                     @Parameter(description = "id заказа") String id) {
        OrdersEntity dto = ordersService.getOrderById(id);
        return new ResponseEntity<>(OrdersMapper.toDTO(dto), HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех заказов")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @ApiResponse(responseCode = "404 NotFound", description = "Заказ не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<Collection<OrderInfoDTO>> getAllOrders(@RequestParam(required = false, defaultValue = "0")
                                                     @Parameter(description = "min: 0")
                                                     @Validated @Min(0) int page,
                                                 @RequestParam(required = false, defaultValue = "10")
                                                     @Parameter(description = "min: 1")
                                                     @Validated @Min(1) int size) {
        if (page < 0 || size < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Collection<OrderInfoDTO> order = ordersService.getAllOrders(page, size)
                .get()
                .map(OrdersMapper::toDTO)
                .collect(Collectors.toList());

        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(order);
    }

    @ExceptionHandler(CountZeroException.class)
    public final ResponseEntity<Error> handleCountZeroException(CountZeroException ex) {
        Error error = new Error(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}