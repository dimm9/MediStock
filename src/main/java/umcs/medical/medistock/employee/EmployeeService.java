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
    private final EmployeeHistoryRepository historyRepository;

    public List<EmployeeDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    //ADMIN: wszyscy (active + inactive)
    public List<EmployeeDTO> getAllForAdmin() {
        return repository.findAllIncludingInactive()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public EmployeeDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    //ADMIN: także soft-deleted
    public EmployeeDTO getByIdForAdmin(Long id) {
        return repository.findByIdIncludingInactive(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public EmployeeDTO create(EmployeeDTO dto, String rawPassword) {
        Employee employee = mapper.toEntity(dto);
        employee.setPasswordHash(passwordEncoder.encode(rawPassword));
        Employee saved = repository.save(employee);
        logHistory("CREATE", "Utworzono pracownika: " + saved.getLogin(), saved.getId());
        return mapper.toDTO(saved);
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee employee = repository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(dto.getName());
        employee.setRole(dto.getRole());
        employee.setSalary(dto.getSalary());
        employee.setLogin(dto.getLogin());
        employee.setHospitalId(dto.getHospitalId());
        employee.setActive(dto.isActive());
        logHistory("UPDATE", "Zaktualizowano dane pracownika: " + employee.getLogin(), id);
        return mapper.toDTO(repository.save(employee));
    }
    public EmployeeDTO changeRole(Long id, EmployeeRole role) {
        Employee employee = repository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String oldRole = employee.getRole().name();
        employee.setRole(role);
        repository.save(employee);

        logHistory("ROLE_CHANGE", "Zmiana roli " + employee.getLogin() + " z " + oldRole + " na " + role.name(), id);
        return mapper.toDTO(employee);
    }

    // soft delete
    public void delete(Long id) {
        Employee e = repository.findById(id).orElse(null);
        repository.deleteById(id);
        if(e != null) {
            logHistory("DELETE", "Usunięto pracownika: " + e.getLogin(), id);
        }
    }

    // restore
    public void restore(Long id) {
        repository.restore(id);
        logHistory("RESTORE", "Przywrócono pracownika ID: " + id, id);
    }

    private void logHistory(String type, String desc, Long targetId) {
        historyRepository.save(EmployeeHistory.builder()
                .actionType(type)
                .description(desc)
                .targetEmployeeId(targetId)
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    public java.util.List<EmployeeHistory> getHistory() {
        return historyRepository.findAllByOrderByTimestampDesc();
    }
}