package pharmacy.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.StockDTO;
import pharmacy.pharmacy.entity.Stock;
import pharmacy.pharmacy.service.StockService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

//    // Add new stock
//    @PostMapping("/add")
//    public Stock addStock(@RequestParam UUID productId, @RequestParam UUID supplierId, @RequestParam int quantityAdded) {
//        return stockService.addStock(productId, supplierId, quantityAdded);
//    }

    // Get all stocks
//    @GetMapping
//    public List<StockDTO> getAllStock() {
//        return stockService.getAllStock();
//    }

//    // Get stock by ID
//    @GetMapping("/{id}")
//    public StockDTO getStockById(@PathVariable UUID id) {
//        return stockService.getStockById(id);
//    }

    // Delete stock by ID
    @DeleteMapping("/{id}")
    public String deleteStock(@PathVariable UUID id) {
        stockService.deleteStock(id);
        return "Stock deleted successfully";
    }
}
