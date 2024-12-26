package ru.eriknas.brokenstore.dto.store.orders;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Создание заказа")
public class OrderCreateDTO {

    @Schema(hidden = true)
    private Integer id;

    @Schema(description = "Идентификатор пользователя")
    @NotNull(message = "Укажите идентификатор пользователя")
    private Integer userId;

    @Schema(description = "Список заказанных футболок с указанием количества")
    @NotNull(message = "Укажите футболки и их количество")
    private List<TShirtOrderDTO> tShirtOrders;

    @Schema(description = "Дата доставки заказа")
    private LocalDate dataDelivery;
}