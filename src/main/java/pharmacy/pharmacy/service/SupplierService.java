package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.SupplierRepository;
import pharmacy.pharmacy.dto.SupplierDTO;
import pharmacy.pharmacy.entity.Supplier;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final UserService userService;

    public SupplierService(SupplierRepository supplierRepository, UserService userService) {
        this.supplierRepository = supplierRepository;
        this.userService = userService;
    }

    public List<SupplierDTO> getAllSuppliers() {
        try {
            return supplierRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToSupplierDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all suppliers", e);
        }
    }

    public SupplierDTO getSupplierById(Integer id) {
        try {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
            return EntityDtoMapper.convertToSupplierDTO(supplier);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve supplier with id: " + id, e);
        }
    }

    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        try {
            // Basic validation
            if (supplierDTO.getCompanyName() == null || supplierDTO.getCompanyName().isEmpty()) {
                throw new GlobalException("Company name cannot be empty");
            }

            User user = null;
            if (supplierDTO.getUserId() != null) {
                user = userService.getUserEntityById(supplierDTO.getUserId());
            }

            Supplier supplier = EntityDtoMapper.convertToSupplier(supplierDTO, user);
            Supplier savedSupplier = supplierRepository.save(supplier);
            return EntityDtoMapper.convertToSupplierDTO(savedSupplier);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create supplier", e);
        }
    }

    public SupplierDTO updateSupplier(Integer id, SupplierDTO supplierDTO) {
        try {
            Supplier existingSupplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

            if (supplierDTO.getCompanyName() != null) {
                existingSupplier.setCompanyName(supplierDTO.getCompanyName());
            }
            if (supplierDTO.getAddress() != null) {
                existingSupplier.setAddress(supplierDTO.getAddress());
            }
            if (supplierDTO.getTaxId() != null) {
                existingSupplier.setTaxId(supplierDTO.getTaxId());
            }
            if (supplierDTO.getUserId() != null) {
                User user = userService.getUserEntityById(supplierDTO.getUserId());
                existingSupplier.setUser(user);
            }

            Supplier updatedSupplier = supplierRepository.save(existingSupplier);
            return EntityDtoMapper.convertToSupplierDTO(updatedSupplier);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update supplier with id: " + id, e);
        }
    }

    public void deleteSupplier(Integer id) {
        try {
            if (!supplierRepository.existsById(id)) {
                throw new ResourceNotFoundException("Supplier not found with id: " + id);
            }
            supplierRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete supplier with id: " + id, e);
        }
    }

    // For internal use by other services
    public Supplier getSupplierEntityById(Integer id) {
        try {
            return supplierRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve supplier entity with id: " + id, e);
        }
    }
}