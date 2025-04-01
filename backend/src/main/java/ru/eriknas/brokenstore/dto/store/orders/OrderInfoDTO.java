package ru.eriknas.brokenstore.dto.store.orders;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "Информация о заказе")
public class OrderInfoDTO {

    @Schema(description = "Идентификатор заказа")
    private Integer id;

    @Schema(description = "Идентификатор пользователя")
    private Integer userId;

    @Schema(description = "Список заказанных футболок")
    private List<TShirtOrderDTO> tShirtOrders;

    @Schema(description = "Стоимость заказа")
    private Double sumOrder;

    @Schema(description = "Дата создания заказа")
    private LocalDate dataOrder;

    @Schema(description = "Дата доставки заказа")
    private LocalDate dataDelivery;

    @Schema(description = "Статус заказа")
    private String statusOrder;

    @Schema(description = "Дата возврата заказа")
    private OffsetDateTime dataReturn;

    @Schema(description = "Причина возврата заказа")
    private String reasonForReturn;
}