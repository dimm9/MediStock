package umcs.medical.medistock.employee;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private Long id;
    private Long hospitalId;
    private String name;
    private EmployeeRole role;
    private BigDecimal salary;
    private String login;
    private boolean active;
}
