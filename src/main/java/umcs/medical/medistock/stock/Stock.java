package umcs.medical.medistock.stock;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private StockCategory category;
}
