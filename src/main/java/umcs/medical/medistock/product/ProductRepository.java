package umcs.medical.medistock.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockId(Long stockId);
    List<Product> findByStockIdAndAvailableTrue(Long stockId);
    Optional<Product> findByIdAndAvailableTrue(Long id);
}
