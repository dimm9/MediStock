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

    public ProductDTO create(ProductDTO dto) {
        Product entity = productMapper.toEntity(dto);
        return productMapper.toDto(productRepository.save(entity));
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productMapper.updateEntityFromDto(dto, entity);
        return productMapper.toDto(productRepository.save(entity));
    }

    // USER:  zmniejsza ilość
    public void use(Long productId, Long userId, int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < amount)
            throw new RuntimeException("Not enough items in stock");

        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);

        // zapis do historii
        ProductUsage usage = ProductUsage.builder()
                .productId(productId)
                .userId(userId)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .adminAction(false)
                .build();

        usageRepository.save(usage);
    }

    // ADMIN: ustawia ilość ręcznie
    public void setQuantity(Long productId, int quantity, Long adminId) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int oldQuantity = product.getQuantity();
        product.setQuantity(quantity);
        productRepository.save(product);

        ProductUsage usage = ProductUsage.builder()
                .productId(productId)
                .userId(adminId)
                .amount(quantity - oldQuantity)
                .timestamp(LocalDateTime.now())
                .adminAction(true)
                .build();

        usageRepository.save(usage);
    }

    public void delete(Long id, Long adminId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductUsage usage = ProductUsage.builder()
                .productId(id)
                .userId(adminId)
                .amount(-product.getQuantity())
                .timestamp(LocalDateTime.now())
                .adminAction(true)
                .build();

        usageRepository.save(usage);

        productRepository.delete(product);
    }

    public void updateProductImage(Long id, String mediaUrl) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setMediaUrl(mediaUrl);
        productRepository.save(product);
    }

    // HISTORIA
    public List<ProductUsage> getUserHistory(Long userId) {
        return usageRepository.findByUserId(userId);
    }

    public List<ProductUsage> getAdminHistory() {
        return usageRepository.findByAdminActionTrue();
    }
}
