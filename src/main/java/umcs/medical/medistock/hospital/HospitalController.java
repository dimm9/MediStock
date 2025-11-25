package umcs.medical.medistock.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService service;

    @PostMapping("/add")
    public ResponseEntity<HospitalDto> create(@RequestBody HospitalDto dto) {
        HospitalDto created = service.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{id}")
    public HospitalDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/all")
    public List<HospitalDto> getAll() {
        return service.listAll();
    }

    @PutMapping("/update/{id}")
    public HospitalDto update(@PathVariable Long id, @RequestBody HospitalDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
