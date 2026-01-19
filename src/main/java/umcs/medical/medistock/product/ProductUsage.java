package umcs.medical.medistock.product;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // id osoby uzwwajacej

    @Column(nullable = false)
    private int amount;  // ilosc uzyta
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean adminAction; // true jesli admin ustawil ilość lub zrobil delete
}