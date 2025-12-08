package umcs.medical.medistock.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public List<StockDTO> getAll() {
        return stockRepository.findAll()
                .stream()
                .map(stockMapper::toDto)
                .toList();
    }

    public StockDTO getById(Long id) {
        return stockRepository.findById(id)
                .map(stockMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    public StockDTO create(StockDTO dto) {
        Stock entity = stockMapper.toEntity(dto);
        Stock saved = stockRepository.save(entity);
        return stockMapper.toDto(saved);
    }

    public StockDTO update(Long id, StockDTO dto) {
        Stock entity = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stockMapper.updateEntityFromDto(dto, entity);
        Stock saved = stockRepository.save(entity);
        return stockMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new RuntimeException("Stock not found");
        }
        stockRepository.deleteById(id);
    }

    public List<StockDTO> getByHospital(Long hospitalId) {
        return stockRepository.findByHospitalId(hospitalId)
                .stream()
                .map(stockMapper::toDto)
                .toList();
    }
}
