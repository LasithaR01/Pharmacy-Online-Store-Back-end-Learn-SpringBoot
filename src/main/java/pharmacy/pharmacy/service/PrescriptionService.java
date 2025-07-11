package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.PrescriptionRepository;
import pharmacy.pharmacy.dto.PrescriptionDTO;
import pharmacy.pharmacy.entity.Prescription;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.enums.PrescriptionStatus;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final UserService userService;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                             UserService userService) {
        this.prescriptionRepository = prescriptionRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getAllPrescriptions() {
        try {
            return prescriptionRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToPrescriptionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all prescriptions", e);
        }
    }

    @Transactional(readOnly = true)
    public PrescriptionDTO getPrescriptionById(int id) {
        try {
            Prescription prescription = prescriptionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
            return EntityDtoMapper.convertToPrescriptionDTO(prescription);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescription with id: " + id, e);
        }
    }

    public Prescription getPrescriptionEntityById(int id) {
        try {
            return prescriptionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescription entity with id: " + id, e);
        }
    }

    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        try {
            validatePrescriptionDTO(prescriptionDTO);

            User user = userService.getUserEntityById(prescriptionDTO.getUserId());
            Prescription prescription = EntityDtoMapper.convertToPrescription(prescriptionDTO, user, null);

            Prescription savedPrescription = prescriptionRepository.save(prescription);
            return EntityDtoMapper.convertToPrescriptionDTO(savedPrescription);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create prescription", e);
        }
    }

    public PrescriptionDTO approvePrescription(int id, int approvedById) {
        try {
            Prescription prescription = getPrescriptionEntityById(id);
            User approvedBy = userService.getUserEntityById(approvedById);

            if (!prescription.canBeApproved()) {
                throw new GlobalException("Prescription cannot be approved in current status");
            }

            prescription.approve(approvedBy);
            Prescription updatedPrescription = prescriptionRepository.save(prescription);
            return EntityDtoMapper.convertToPrescriptionDTO(updatedPrescription);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to approve prescription with id: " + id, e);
        }
    }

    public PrescriptionDTO rejectPrescription(int id, int rejectedById) {
        try {
            Prescription prescription = getPrescriptionEntityById(id);
            User rejectedBy = userService.getUserEntityById(rejectedById);

            if (!prescription.canBeRejected()) {
                throw new GlobalException("Prescription cannot be rejected in current status");
            }

            prescription.reject(rejectedBy);
            Prescription updatedPrescription = prescriptionRepository.save(prescription);
            return EntityDtoMapper.convertToPrescriptionDTO(updatedPrescription);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to reject prescription with id: " + id, e);
        }
    }

    public void deletePrescription(int id) {
        try {
            Prescription prescription = getPrescriptionEntityById(id);

            if (prescription.getStatus() != PrescriptionStatus.PENDING) {
                throw new GlobalException("Only pending prescriptions can be deleted");
            }

            prescriptionRepository.delete(prescription);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete prescription with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPrescriptionsByUser(int userId) {
        try {
            return prescriptionRepository.findByUserId(userId).stream()
                    .map(EntityDtoMapper::convertToPrescriptionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescriptions for user id: " + userId, e);
        }
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getPrescriptionsByStatus(PrescriptionStatus status) {
        try {
            return prescriptionRepository.findByStatus(status).stream()
                    .map(EntityDtoMapper::convertToPrescriptionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve prescriptions by status: " + status, e);
        }
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getExpiringPrescriptions(Date expiryDate) {
        try {
            return prescriptionRepository.findExpiringPrescriptions(expiryDate).stream()
                    .map(EntityDtoMapper::convertToPrescriptionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve expiring prescriptions", e);
        }
    }

    @Transactional(readOnly = true)
    public long countByStatus(PrescriptionStatus status) {
        try {
            return prescriptionRepository.countByStatus(status);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to count prescriptions by status", e);
        }
    }

    private void validatePrescriptionDTO(PrescriptionDTO dto) {
        if (dto.getUserId() == null) {
            throw new GlobalException("User ID is required");
        }
        if (dto.getDoctorName() == null || dto.getDoctorName().trim().isEmpty()) {
            throw new GlobalException("Doctor name is required");
        }
        if (dto.getDoctorContact() == null || dto.getDoctorContact().trim().isEmpty()) {
            throw new GlobalException("Doctor contact is required");
        }
        if (dto.getPrescriptionDate() == null) {
            throw new GlobalException("Prescription date is required");
        }
    }
}