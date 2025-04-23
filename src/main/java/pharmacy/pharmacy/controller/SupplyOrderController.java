//package pharmacy.pharmacy.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import pharmacy.pharmacy.dto.SupplyOrderRequestDTO;
//import pharmacy.pharmacy.dto.SupplyOrderResponseDTO;
//import pharmacy.pharmacy.service.SupplyOrderService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/supply-orders")
//public class SupplyOrderController {
//
//    private final SupplyOrderService supplyOrderService;
//
//    public SupplyOrderController(SupplyOrderService supplyOrderService) {
//        this.supplyOrderService = supplyOrderService;
//    }
//
//    // Create a new supply order
//    @PostMapping
//    public ResponseEntity<SupplyOrderResponseDTO> createSupplyOrder(@RequestBody SupplyOrderRequestDTO requestDTO) {
//        return ResponseEntity.ok(supplyOrderService.createSupplyOrder(requestDTO));
//    }
//
//    // Get all supply orders
//    @GetMapping
//    public ResponseEntity<List<SupplyOrderResponseDTO>> getAllSupplyOrders() {
//        return ResponseEntity.ok(supplyOrderService.getAllSupplyOrders());
//    }
//
//    // Get a single supply order by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<SupplyOrderResponseDTO> getSupplyOrderById(@PathVariable UUID id) {
//        Optional<SupplyOrderResponseDTO> supplyOrder = supplyOrderService.getSupplyOrderById(id);
//        return supplyOrder.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Update a supply order
//    @PutMapping("/{id}")
//    public ResponseEntity<SupplyOrderResponseDTO> updateSupplyOrder(@PathVariable UUID id, @RequestBody SupplyOrderRequestDTO requestDTO) {
//        return ResponseEntity.ok(supplyOrderService.updateSupplyOrder(id, requestDTO));
//    }
//
//    // Delete a supply order
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSupplyOrder(@PathVariable UUID id) {
//        supplyOrderService.deleteSupplyOrder(id);
//        return ResponseEntity.noContent().build();
//    }
//}
