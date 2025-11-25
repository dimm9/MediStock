package umcs.medical.medistock.hospital;

 import java.math.BigDecimal;

 import jakarta.persistence.*;
 import lombok.Getter;
 import lombok.Setter;
 import org.hibernate.annotations.Check;

 @Getter
 @Setter
 @Entity
 @Table(name = "hospital")
 @Check(constraints = "funds >= 0")
 public class Hospital {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(nullable = false)
     private String name;

     @Column(nullable = false, precision = 14, scale = 2)
     private BigDecimal funds;

     public Hospital() {}

     public Hospital(Long id, String name, BigDecimal funds) {
         this.id = id;
         this.name = name;
         this.funds = funds;
     }

 }