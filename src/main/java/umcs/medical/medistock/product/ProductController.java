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
    public ProductDTO create(
            @RequestBody ProductDTO dto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return productService.create(dto, principal.getId());
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            ProductDTO product = productService.getById(id);
            String productName = product.getName().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_\\-]", "");
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = ".png";
            }
            String fileName = productName + extension;
            Path targetPath = Paths.get("target/classes/static/images");
            if (!Files.exists(targetPath)) Files.createDirectories(targetPath);
            Files.copy(file.getInputStream(), targetPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            Path sourcePath = Paths.get("src/main/resources/static/images");
            if (!Files.exists(sourcePath)) Files.createDirectories(sourcePath);
            Files.copy(file.getInputStream(), sourcePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            String mediaUrl = "/images/" + fileName;
            productService.updateProductImage(id, mediaUrl, principal.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping("/{id}")
    public ProductDTO update(
            @PathVariable Long id,
            @RequestBody ProductDTO dto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return productService.update(id, dto, principal.getId());
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