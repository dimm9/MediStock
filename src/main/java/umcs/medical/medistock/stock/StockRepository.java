package umcs.medical.medistock.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByHospitalId(Long hospitalId);
}
