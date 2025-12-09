package umcs.medical.medistock.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<EmployeeDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public EmployeeDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public EmployeeDTO create(EmployeeDTO dto, String rawPassword) {
        Employee employee = mapper.toEntity(dto);
        employee.setPasswordHash(passwordEncoder.encode(rawPassword));
        Employee saved = repository.save(employee);
        return mapper.toDTO(saved);
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setSalary(dto.getSalary());
        employee.setLogin(dto.getLogin());
        employee.setActive(dto.isActive());
        employee.setHospitalId(dto.getHospitalId());

        Employee saved = repository.save(employee);
        return mapper.toDTO(saved);
    }

    public EmployeeDTO changeRole(Long id, EmployeeRole role) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setRole(role);
        repository.save(employee);

        return mapper.toDTO(employee);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}