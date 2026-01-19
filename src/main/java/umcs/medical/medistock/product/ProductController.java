package umcs.medical.medistock.product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umcs.medical.medistock.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ---------------- USER ----------------
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
            @RequestParam int amount,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        productService.use(id, principal.getId(), amount);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history/user")
    public List<ProductUsage> getUserHistory(@AuthenticationPrincipal UserPrincipal principal) {
        return productService.getUserHistory(principal.getId());
    }

    // ---------------- ADMIN ----------------
    @GetMapping("/all")
    public List<ProductDTO> getAll() {
        return productService.getAll();
    }

    @PutMapping("/admin/{id}/quantity")
    public ResponseEntity<Void> setQuantity(
            @PathVariable Long id,
            @RequestParam int value,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        productService.setQuantity(id, value, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        productService.delete(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history/admin")
    public List<ProductUsage> getAdminHistory() {
        return productService.getAdminHistory();
    }
}