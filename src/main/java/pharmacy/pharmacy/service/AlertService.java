package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.AlertRepository;
import pharmacy.pharmacy.dto.AlertDTO;
import pharmacy.pharmacy.entity.*;
import pharmacy.pharmacy.enums.AlertStatus;
import pharmacy.pharmacy.enums.AlertType;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final ProductService productService;
    private final BranchService branchService;
    private final UserService userService;

    public AlertService(AlertRepository alertRepository,
                      ProductService productService,
                      BranchService branchService,
                      UserService userService) {
        this.alertRepository = alertRepository;
        this.productService = productService;
        this.branchService = branchService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<AlertDTO> getAllAlerts() {
        try {
            return alertRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToAlertDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve alerts", e);
        }
    }

    @Transactional(readOnly = true)
    public AlertDTO getAlertById(int id) {
        try {
            Alert alert = alertRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
            return EntityDtoMapper.convertToAlertDTO(alert);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve alert with id: " + id, e);
        }
    }

    public Alert getAlertEntityById(int id) {
        try {
            return alertRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve alert entity with id: " + id, e);
        }
    }

    public AlertDTO createAlert(AlertDTO alertDTO) {
        try {
            validateAlertDTO(alertDTO);

            Product product = alertDTO.getProductId() != null ?
                productService.getProductEntityById(alertDTO.getProductId()) : null;
            Branch branch = alertDTO.getBranchId() != null ?
                branchService.getBranchEntityById(alertDTO.getBranchId()) : null;
            User triggeredBy = userService.getUserEntityById(alertDTO.getTriggeredById());

            Alert alert = EntityDtoMapper.convertToAlert(alertDTO, product, branch, triggeredBy, null);
            alert.setStatus(AlertStatus.ACTIVE);

            Alert savedAlert = alertRepository.save(alert);
            return EntityDtoMapper.convertToAlertDTO(savedAlert);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create alert", e);
        }
    }

    public AlertDTO resolveAlert(int id, int resolvedById) {
        try {
            Alert alert = getAlertEntityById(id);
            User resolvedBy = userService.getUserEntityById(resolvedById);

            alert.resolve(resolvedBy);
            Alert updatedAlert = alertRepository.save(alert);
            return EntityDtoMapper.convertToAlertDTO(updatedAlert);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to resolve alert with id: " + id, e);
        }
    }

    public void resolveMultipleAlerts(List<Integer> ids, int resolvedById) {
        try {
            User resolvedBy = userService.getUserEntityById(resolvedById);
            alertRepository.resolveAlerts(ids, resolvedBy);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to resolve alerts", e);
        }
    }

    public AlertDTO reopenAlert(int id) {
        try {
            Alert alert = getAlertEntityById(id);
            alert.reopen();
            Alert updatedAlert = alertRepository.save(alert);
            return EntityDtoMapper.convertToAlertDTO(updatedAlert);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to reopen alert with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<AlertDTO> getUnresolvedAlerts() {
        try {
            return alertRepository.findByResolvedFalse().stream()
                    .map(EntityDtoMapper::convertToAlertDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve unresolved alerts", e);
        }
    }

    @Transactional(readOnly = true)
    public List<AlertDTO> getCriticalAlerts() {
        try {
            return alertRepository.findCriticalAlerts().stream()
                    .map(EntityDtoMapper::convertToAlertDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve critical alerts", e);
        }
    }

    @Transactional(readOnly = true)
    public long countUnresolvedAlerts() {
        try {
            return alertRepository.countByResolvedFalse();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to count unresolved alerts", e);
        }
    }

    public void cleanupResolvedAlerts(Date cutoffDate) {
        try {
            alertRepository.deleteResolvedOlderThan(cutoffDate);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to cleanup resolved alerts", e);
        }
    }

    private void validateAlertDTO(AlertDTO dto) {
        if (dto.getAlertType() == null) {
            throw new GlobalException("Alert type is required");
        }
        if (dto.getMessage() == null || dto.getMessage().trim().isEmpty()) {
            throw new GlobalException("Alert message is required");
        }
        if (dto.getTriggeredById() == null) {
            throw new GlobalException("Triggered by user ID is required");
        }
    }
}