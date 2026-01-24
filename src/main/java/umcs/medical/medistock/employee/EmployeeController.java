package umcs.medical.medistock.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    //api bez usunietych uzytkownikow
    @GetMapping
    public List<EmployeeDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public EmployeeDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public EmployeeDTO create(@RequestBody EmployeeCreateRequest request) {
        return service.create(request.getEmployee(), request.getPassword());
    }

    @PutMapping("/{id}")
    public EmployeeDTO update(@PathVariable Long id, @RequestBody EmployeeDTO dto) {
        return service.update(id, dto);
    }

    @PutMapping("/{id}/role")
    public EmployeeDTO changeRole(@PathVariable Long id, @RequestParam EmployeeRole role) {
        return service.changeRole(id, role);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // ADMIN API (widzi też inactive)
    // =========================================================

    @GetMapping("/admin")
    public List<EmployeeDTO> getAllForAdmin() {
        return service.getAllForAdmin();
    }

    @GetMapping("/admin/{id}")
    public EmployeeDTO getByIdForAdmin(@PathVariable Long id) {
        return service.getByIdForAdmin(id);
    }

    @PutMapping("/admin/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("admin/history")
    public List<EmployeeHistory> getHistory() {
        return service.getHistory();
    }
}
