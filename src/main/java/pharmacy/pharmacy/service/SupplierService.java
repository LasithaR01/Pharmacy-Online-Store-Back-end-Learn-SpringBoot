package pharmacy.pharmacy.service;

import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dto.SupplierRequest;
import pharmacy.pharmacy.dto.SupplierResponse;
import pharmacy.pharmacy.entity.Supplier;
import pharmacy.pharmacy.exception.SupplierNotFoundException;
import pharmacy.pharmacy.exception.SupplierAlreadyExistsException;
import pharmacy.pharmacy.dao.SupplierRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // Convert Supplier Entity to Response DTO
    private SupplierResponse convertToDto(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactNumber(),
                supplier.getAddress(),
                supplier.getCreatedAt()
        );
    }

    // Create Supplier
    public SupplierResponse createSupplier(SupplierRequest requestDto) {
        // Check if supplier already exists by name (you can change this check as needed)
        if (supplierRepository.existsByName(requestDto.getName())) {
            throw new SupplierAlreadyExistsException("Supplier with name '" + requestDto.getName() + "' already exists.");
        }

        Supplier supplier = new Supplier(
                requestDto.getName(),
                requestDto.getContactNumber(),
                requestDto.getAddress(),
                new java.util.Date()
        );

        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDto(savedSupplier);
    }

    // Get all suppliers
    public List<SupplierResponse> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Get supplier by ID
    public SupplierResponse getSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with ID " + id + " not found."));
        return convertToDto(supplier);
    }

    // Update supplier
    public SupplierResponse updateSupplier(UUID id, SupplierRequest requestDto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with ID " + id + " not found."));

        supplier.setName(requestDto.getName());
        supplier.setContactNumber(requestDto.getContactNumber());
        supplier.setAddress(requestDto.getAddress());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDto(updatedSupplier);
    }

    // Delete supplier
    public void deleteSupplier(UUID id) {
        if (!supplierRepository.existsById(id)) {
            throw new SupplierNotFoundException("Supplier with ID " + id + " not found.");
        }
        supplierRepository.deleteById(id);
    }
}
