package umcs.medical.medistock.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //user
    @GetMapping("/stock/{stockId}")
    public List<ProductDTO> getAvailableByStock(@PathVariable Long stockId) {
        return productService.getProductsByStock(stockId);
    }
    @GetMapping("/{id}")
    public ProductDTO getAvailableById(@PathVariable Long id) {
        return productService.getActiveById(id);
    }

    @PostMapping("/use/{id}")
    public ResponseEntity<Void> use(
            @PathVariable Long id,
            @RequestParam int amount
    ) {
        productService.use(id, amount);
        return ResponseEntity.noContent().build();
    }

    // ADMIN
    @GetMapping("/all")
    public List<ProductDTO> getAll() {
        return productService.getAll();
    }

    @PutMapping("/admin/{id}/quantity")
    public ResponseEntity<Void> setQuantity(
            @PathVariable Long id,
            @RequestParam int value
    ) {
        productService.setQuantity(id, value);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
