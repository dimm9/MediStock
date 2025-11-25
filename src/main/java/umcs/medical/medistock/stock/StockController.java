package umcs.medical.medistock.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("/all")
    public List<StockDTO> getAll() {
        return stockService.getAll();
    }

    @GetMapping("/{id}")
    public StockDTO getById(@PathVariable Long id) {
        return stockService.getById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<StockDTO> create(@RequestBody StockDTO dto) {
        return ResponseEntity.status(201).body(stockService.create(dto));
    }

    @PutMapping("/update/{id}")
    public StockDTO update(@PathVariable Long id, @RequestBody StockDTO dto) {
        return stockService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
