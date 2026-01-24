package umcs.medical.medistock.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductUsageRepository usageRepository;

    //user
    public List<ProductDTO> getProductsByStock(Long stockId) {
        return productRepository.findByStockIdAndAvailableTrue(stockId)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
    public ProductDTO getActiveById(Long id) {
        return productRepository.findByIdAndAvailableTrue(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found or unavailable"));
    }

    // admin pobiera wszystkie
    public List<ProductDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDTO getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDTO create(ProductDTO dto, Long adminId) {
        Product entity = productMapper.toEntity(dto);
        Product saved = productRepository.save(entity);
        saveLog(saved.getId(), adminId, saved.getQuantity(), "Utworzono produkt: " + saved.getName());
        return productMapper.toDto(saved);
    }

    public ProductDTO update(Long id, ProductDTO dto, Long adminId) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        String oldDetails = entity.getName() + " (" + entity.getQuantity() + " szt.)";
        productMapper.updateEntityFromDto(dto, entity);
        Product saved = productRepository.save(entity);
        saveLog(saved.getId(), adminId, 0, "Edytowano produkt. Było: " + oldDetails);
        return productMapper.toDto(saved);
    }

    public void use(Long productId, Long userId, int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < amount)
            throw new RuntimeException("Not enough items in stock");
        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);
        saveLog(productId, userId, amount, "Pobrano: " + product.getName(), false);
    }

    public void setQuantity(Long productId, int quantity, Long adminId) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int diff = quantity - product.getQuantity();
        product.setQuantity(quantity);
        productRepository.save(product);
        saveLog(productId, adminId, diff, "Korekta stanu magazynowego");
    }

    public void delete(Long id, Long adminId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        saveLog(id, adminId, -product.getQuantity(), "Usunięto produkt: " + product.getName());
        productRepository.delete(product);
    }

    public void updateProductImage(Long id, String mediaUrl, Long adminId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setMediaUrl(mediaUrl);
        productRepository.save(product);
        saveLog(id, adminId, 0, "Zaktualizowano zdjęcie produktu");
    }

    // HISTORIA
    public List<ProductUsage> getUserHistory(Long userId) {
        return usageRepository.findByUserId(userId);
    }

    public List<ProductUsage> getAdminHistory() {
        return usageRepository.findByAdminActionTrue();
    }

    private void saveLog(Long prodId, Long userId, int amount, String desc) {
        saveLog(prodId, userId, amount, desc, true);
    }

    private void saveLog(Long prodId, Long userId, int amount, String desc, boolean isAdmin) {
        ProductUsage usage = ProductUsage.builder()
                .productId(prodId)
                .userId(userId)
                .amount(amount)
                .description(desc) // opis
                .timestamp(LocalDateTime.now())
                .adminAction(isAdmin)
                .build();
        usageRepository.save(usage);
    }
}

