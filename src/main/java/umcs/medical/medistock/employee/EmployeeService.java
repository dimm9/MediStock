package umcs.medical.medistock.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmployeeMapper mapper = EmployeeMapper.INSTANCE;

    public List<EmployeeDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public EmployeeDTO create(EmployeeDTO dto, String rawPassword) {
        Employee employee = mapper.toEntity(dto);
        employee.setPasswordHash(passwordEncoder.encode(rawPassword));
        return mapper.toDTO(repository.save(employee));
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setSalary(dto.getSalary());
        employee.setLogin(dto.getLogin());
        employee.setActive(dto.isActive());
        return mapper.toDTO(repository.save(employee));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}