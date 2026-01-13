package umcs.medical.medistock.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
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
    public void use(Long productId, int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < amount)
            throw new RuntimeException("Not enough items in stock");

        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);
    }

    // ADMIN: ustawia ilość ręcznie
    public void setQuantity(Long productId, int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity cannot be negative");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity(quantity);
        productRepository.save(product);
    }

    // ADMIN: hard delete

    public void delete(Long id) {
        if (!productRepository.existsById(id))
            throw new RuntimeException("Product not found");

        productRepository.deleteById(id);
    }

}
