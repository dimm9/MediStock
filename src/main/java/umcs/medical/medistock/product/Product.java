package umcs.medical.medistock.product;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @Column(name = "media_url")
    private String mediaUrl;
}

