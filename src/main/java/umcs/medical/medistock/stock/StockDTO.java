package umcs.medical.medistock.stock;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {
    private Long id;
    private String name;
    private Long hospitalId;
}
