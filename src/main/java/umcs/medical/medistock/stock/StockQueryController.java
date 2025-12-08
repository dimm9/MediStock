package umcs.medical.medistock.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockQueryController {

    private final StockService stockService;

    @GetMapping
    public List<StockDTO> getByHospital(@RequestParam Long hospitalId) {
        return stockService.getByHospital(hospitalId);
    }
}
