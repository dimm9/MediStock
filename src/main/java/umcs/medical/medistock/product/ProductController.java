package umcs.medical.medistock.product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umcs.medical.medistock.security.UserPrincipal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/add")
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return productService.create(dto);
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("src/main/resources/static/images");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String mediaUrl = "/images/" + fileName;
            productService.updateProductImage(id, mediaUrl);
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Użytkownik nie jest zalogowany");
        }
        productService.delete(id, principal.getId());
        return ResponseEntity.ok("Product deleted");
    }


    @GetMapping("/history/admin")
    public List<ProductUsage> getAdminHistory() {
        return productService.getAdminHistory();
    }
}