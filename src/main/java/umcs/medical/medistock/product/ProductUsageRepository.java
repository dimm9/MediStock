package umcs.medical.medistock.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductUsageRepository extends JpaRepository<ProductUsage, Long> {
    List<ProductUsage> findByProductId(Long productId);
    List<ProductUsage> findByUserId(Long userId);
    List<ProductUsage> findByAdminActionTrue(); // tylko akcje admina
}