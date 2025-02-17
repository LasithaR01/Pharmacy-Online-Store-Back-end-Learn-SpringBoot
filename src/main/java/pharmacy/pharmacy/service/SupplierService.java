package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dto.SupplierRequest;
import pharmacy.pharmacy.dto.SupplierResponse;
import pharmacy.pharmacy.entity.Supplier;
import pharmacy.pharmacy.dao.SupplierRepository;
import pharmacy.pharmacy.exception.SupplierNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // Create a new supplier
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactNumber(request.getContactNumber());
        supplier.setAddress(request.getAddress());

        Supplier savedSupplier = supplierRepository.save(supplier);
        return mapToResponse(savedSupplier);
    }

    // Get all suppliers
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get supplier by ID
    public SupplierResponse getSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with ID: " + id));

        return mapToResponse(supplier);
    }

    // Update an existing supplier
    public SupplierResponse updateSupplier(UUID id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with ID: " + id));

        supplier.setName(request.getName());
        supplier.setContactNumber(request.getContactNumber());
        supplier.setAddress(request.getAddress());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return mapToResponse(updatedSupplier);
    }

    // Delete supplier by ID
    public void deleteSupplier(UUID id) {
        if (!supplierRepository.existsById(id)) {
            throw new SupplierNotFoundException("Supplier not found with ID: " + id);
        }
        supplierRepository.deleteById(id);
    }

    // Helper method to map Supplier entity to SupplierResponse DTO
    private SupplierResponse mapToResponse(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactNumber(),
                supplier.getAddress()
        );
    }
}
