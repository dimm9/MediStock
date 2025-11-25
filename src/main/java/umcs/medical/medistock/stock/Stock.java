package umcs.medical.medistock.stock;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import umcs.medical.medistock.hospital.Hospital;

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

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
