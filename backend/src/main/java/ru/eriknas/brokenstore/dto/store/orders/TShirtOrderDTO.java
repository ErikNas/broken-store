package ru.eriknas.brokenstore.dto.store.orders;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Информация о заказе футболки")
public class TShirtOrderDTO {

    @Schema(description = "Идентификатор футболки")
    @NotNull(message = "Укажите идентификатор футболки")
    private Integer tShirtId;

    @Schema(description = "Количество заказанных футболок")
    @NotNull(message = "Укажите количество заказанных футболок")
    private Integer count;
}