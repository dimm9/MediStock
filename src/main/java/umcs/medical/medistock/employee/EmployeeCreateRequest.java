package umcs.medical.medistock.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCreateRequest {
    private EmployeeDTO employee;
    private String password;
}
