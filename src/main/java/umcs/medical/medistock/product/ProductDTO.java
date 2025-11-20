package umcs.medical.medistock.product;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal cost;
    private boolean available;
}
