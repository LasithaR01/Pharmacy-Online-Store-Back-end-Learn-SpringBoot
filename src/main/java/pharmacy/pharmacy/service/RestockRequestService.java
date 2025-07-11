package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.RestockRequestRepository;
import pharmacy.pharmacy.dto.RestockRequestDTO;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.enums.RestockStatus;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestockRequestService {

    private final RestockRequestRepository restockRequestRepository;
    private final ProductService productService;
    private final BranchService branchService;
    private final UserService userService;
    private final SupplierService supplierService;

    public RestockRequestService(RestockRequestRepository restockRequestRepository,
                               ProductService productService,
                               BranchService branchService,
                               UserService userService,
                               SupplierService supplierService) {
        this.restockRequestRepository = restockRequestRepository;
        this.productService = productService;
        this.branchService = branchService;
        this.userService = userService;
        this.supplierService = supplierService;
    }

    @Transactional(readOnly = true)
    public List<RestockRequestDTO> getAllRestockRequests() {
        try {
            return restockRequestRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToRestockRequestDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock requests", e);
        }
    }

    @Transactional(readOnly = true)
    public RestockRequestDTO getRestockRequestById(int id) {
        try {
            RestockRequest request = restockRequestRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Restock request not found with id: " + id));
            return EntityDtoMapper.convertToRestockRequestDTO(request);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock request with id: " + id, e);
        }
    }

    public RestockRequest getRestockRequestEntityById(int id) {
        try {
            return restockRequestRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Restock request not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock request entity with id: " + id, e);
        }
    }

    public RestockRequestDTO createRestockRequest(RestockRequestDTO requestDTO) {
        try {
            validateRestockRequestDTO(requestDTO);

            Product product = productService.getProductEntityById(requestDTO.getProductId());
            Branch branch = branchService.getBranchEntityById(requestDTO.getBranchId());
            User requestedBy = userService.getUserEntityById(requestDTO.getRequestedById());
            Supplier supplier = requestDTO.getSupplierId() != null ?
                supplierService.getSupplierEntityById(requestDTO.getSupplierId()) : null;

            RestockRequest request = RestockRequest.builder()
                    .product(product)
                    .branch(branch)
                    .requestedBy(requestedBy)
                    .quantity(requestDTO.getQuantity())
                    .status(RestockStatus.PENDING)
                    .supplier(supplier)
                    .notes(requestDTO.getNotes())
                    .build();

            RestockRequest savedRequest = restockRequestRepository.save(request);
            return EntityDtoMapper.convertToRestockRequestDTO(savedRequest);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create restock request", e);
        }
    }

    public RestockRequestDTO approveRequest(int id, int approvedById) {
        try {
            RestockRequest request = getRestockRequestEntityById(id);
            User approvedBy = userService.getUserEntityById(approvedById);

            request.approve(approvedBy);
            RestockRequest updatedRequest = restockRequestRepository.save(request);
            return EntityDtoMapper.convertToRestockRequestDTO(updatedRequest);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to approve restock request with id: " + id, e);
        }
    }

    public void approveMultipleRequests(List<Integer> ids, int approvedById) {
        try {
            User approvedBy = userService.getUserEntityById(approvedById);
            restockRequestRepository.approveRequests(ids, approvedBy);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to approve restock requests", e);
        }
    }

    public RestockRequestDTO rejectRequest(int id, int rejectedById) {
        try {
            RestockRequest request = getRestockRequestEntityById(id);
            User rejectedBy = userService.getUserEntityById(rejectedById);

            request.reject(rejectedBy);
            RestockRequest updatedRequest = restockRequestRepository.save(request);
            return EntityDtoMapper.convertToRestockRequestDTO(updatedRequest);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to reject restock request with id: " + id, e);
        }
    }

    public RestockRequestDTO fulfillRequest(int id) {
        try {
            RestockRequest request = getRestockRequestEntityById(id);
            request.fulfill();
            RestockRequest updatedRequest = restockRequestRepository.save(request);
            return EntityDtoMapper.convertToRestockRequestDTO(updatedRequest);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to fulfill restock request with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<RestockRequestDTO> getRequestsByStatus(RestockStatus status) {
        try {
            return restockRequestRepository.findByStatus(status).stream()
                    .map(EntityDtoMapper::convertToRestockRequestDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve restock requests by status", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RestockRequestDTO> getActiveRequests() {
        try {
            return restockRequestRepository.findActiveRequests().stream()
                    .map(EntityDtoMapper::convertToRestockRequestDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve active restock requests", e);
        }
    }

    @Transactional(readOnly = true)
    public long countByStatus(RestockStatus status) {
        try {
            return restockRequestRepository.countByStatus(status);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to count restock requests by status", e);
        }
    }

    private void validateRestockRequestDTO(RestockRequestDTO dto) {
        if (dto.getProductId() == null) {
            throw new GlobalException("Product ID is required");
        }
        if (dto.getBranchId() == null) {
            throw new GlobalException("Branch ID is required");
        }
        if (dto.getRequestedById() == null) {
            throw new GlobalException("Requester ID is required");
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new GlobalException("Quantity must be greater than 0");
        }
    }
}