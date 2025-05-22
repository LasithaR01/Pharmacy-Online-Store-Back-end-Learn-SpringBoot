//package pharmacy.pharmacy.service;
//
//import org.springframework.stereotype.Service;
//import pharmacy.pharmacy.dto.SupplyOrderRequestDTO;
//import pharmacy.pharmacy.dto.SupplyOrderResponseDTO;
////import pharmacy.pharmacy.entity.Branch;
//import pharmacy.pharmacy.entity.Supplier;
//import pharmacy.pharmacy.entity.SupplyOrder;
//import pharmacy.pharmacy.mapper.SupplyOrderMapper;
////import pharmacy.pharmacy.dao.BranchRepository;
//import pharmacy.pharmacy.dao.SupplierRepository;
//import pharmacy.pharmacy.dao.SupplyOrderRepository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class SupplyOrderService {
//
//    private final SupplyOrderRepository supplyOrderRepository;
//    private final SupplierRepository supplierRepository;
////    private final BranchRepository branchRepository;
//
//    public SupplyOrderService(SupplyOrderRepository supplyOrderRepository, SupplierRepository supplierRepository) {
//        this.supplyOrderRepository = supplyOrderRepository;
//        this.supplierRepository = supplierRepository;
////        this.branchRepository = branchRepository;
//    }
//
//    // Create a new supply order
//    public SupplyOrderResponseDTO createSupplyOrder(SupplyOrderRequestDTO requestDTO) {
//        Supplier supplier = supplierRepository.findById(requestDTO.getSupplierId())
//                .orElseThrow(() -> new RuntimeException("Supplier not found"));
//
////        Branch branch = branchRepository.findById(requestDTO.getBranchId())
////                .orElseThrow(() -> new RuntimeException("Branch not found"));
////
////        SupplyOrder supplyOrder = SupplyOrderMapper.toEntity(requestDTO);
////        supplyOrder.setSupplier(supplier);
////        supplyOrder.setBranch(branch);
//
////        SupplyOrder savedOrder = supplyOrderRepository.save(supplyOrder);
//        return SupplyOrderMapper.toResponseDTO(savedOrder);
//    }
//
//    // Get all supply orders
//    public List<SupplyOrderResponseDTO> getAllSupplyOrders() {
//        return supplyOrderRepository.findAll().stream()
//                .map(SupplyOrderMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Get a supply order by ID
//    public Optional<SupplyOrderResponseDTO> getSupplyOrderById(UUID id) {
//        return supplyOrderRepository.findById(id)
//                .map(SupplyOrderMapper::toResponseDTO);
//    }
//
//    // Update a supply order
//    public SupplyOrderResponseDTO updateSupplyOrder(UUID id, SupplyOrderRequestDTO requestDTO) {
//        SupplyOrder existingOrder = supplyOrderRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Supply Order not found"));
//
//        Supplier supplier = supplierRepository.findById(requestDTO.getSupplierId())
//                .orElseThrow(() -> new RuntimeException("Supplier not found"));
//
////        Branch branch = branchRepository.findById(requestDTO.getBranchId())
////                .orElseThrow(() -> new RuntimeException("Branch not found"));
////
////        existingOrder.setSupplier(supplier);
////        existingOrder.setBranch(branch);
////        existingOrder.setOrderDate(requestDTO.getOrderDate());
////        existingOrder.setStatus(requestDTO.getStatus());
//
//        SupplyOrder updatedOrder = supplyOrderRepository.save(existingOrder);
//        return SupplyOrderMapper.toResponseDTO(updatedOrder);
//    }
//
//    // Delete a supply order
//    public void deleteSupplyOrder(UUID id) {
//        supplyOrderRepository.deleteById(id);
//    }
//}
