package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.DrugInteractionRepository;
import pharmacy.pharmacy.dao.ProductRepository;
import pharmacy.pharmacy.dto.DrugInteractionDTO;
import pharmacy.pharmacy.entity.DrugInteraction;
import pharmacy.pharmacy.entity.Product;
import pharmacy.pharmacy.enums.InteractionSeverity;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DrugInteractionService {

    private final DrugInteractionRepository interactionRepository;
    private final ProductRepository productRepository;

    public DrugInteractionService(DrugInteractionRepository interactionRepository,
                                ProductRepository productRepository) {
        this.interactionRepository = interactionRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<DrugInteractionDTO> getAllInteractions() {
        try {
            return interactionRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToDrugInteractionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all drug interactions", e);
        }
    }

    @Transactional(readOnly = true)
    public DrugInteractionDTO getInteractionById(int id) {
        try {
            DrugInteraction interaction = interactionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Drug interaction not found with id: " + id));
            return EntityDtoMapper.convertToDrugInteractionDTO(interaction);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve drug interaction with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<DrugInteractionDTO> getInteractionsForProduct(int productId) {
        try {
            if (!productRepository.existsById(productId)) {
                throw new ResourceNotFoundException("Product not found with id: " + productId);
            }

            return interactionRepository.findByProductInvolved(productId).stream()
                    .map(EntityDtoMapper::convertToDrugInteractionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve interactions for product with id: " + productId, e);
        }
    }

    @Transactional(readOnly = true)
    public List<DrugInteractionDTO> getInteractionsBetweenProducts(int productId1, int productId2) {
        try {
            if (!productRepository.existsById(productId1)) {
                throw new ResourceNotFoundException("Product not found with id: " + productId1);
            }
            if (!productRepository.existsById(productId2)) {
                throw new ResourceNotFoundException("Product not found with id: " + productId2);
            }

            return interactionRepository.findInteractionBetweenProducts(productId1, productId2).stream()
                    .map(EntityDtoMapper::convertToDrugInteractionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException(
                "Failed to retrieve interactions between products with ids: " + productId1 + " and " + productId2, e);
        }
    }

    @Transactional(readOnly = true)
    public List<DrugInteractionDTO> getInteractionsBySeverity(String severity) {
        try {
            InteractionSeverity severityEnum = InteractionSeverity.valueOf(severity.toUpperCase());
            return interactionRepository.findBySeverity(severityEnum).stream()
                    .map(EntityDtoMapper::convertToDrugInteractionDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new GlobalException("Invalid severity level: " + severity);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve interactions by severity: " + severity, e);
        }
    }

    public DrugInteractionDTO createInteraction(DrugInteractionDTO interactionDTO) {
        try {
            // Validate required fields
            if (interactionDTO.getProductId() == null || interactionDTO.getInteractsWithId() == null) {
                throw new GlobalException("Both product IDs must be specified");
            }
            if (interactionDTO.getSeverity() == null) {
                throw new GlobalException("Severity must be specified");
            }
            if (interactionDTO.getDescription() == null || interactionDTO.getDescription().trim().isEmpty()) {
                throw new GlobalException("Description cannot be empty");
            }

            // Check if products exist
            Product product = productRepository.findById(interactionDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + interactionDTO.getProductId()));
            Product interactsWith = productRepository.findById(interactionDTO.getInteractsWithId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + interactionDTO.getInteractsWithId()));

            // Check if interaction already exists
            if (interactionRepository.existsInteractionBetweenProducts(
                    interactionDTO.getProductId(), interactionDTO.getInteractsWithId())) {
                throw new GlobalException("Interaction between these products already exists");
            }

            // Create new interaction
            DrugInteraction interaction = EntityDtoMapper.convertToDrugInteraction(
                    interactionDTO, product, interactsWith);

            DrugInteraction savedInteraction = interactionRepository.save(interaction);
            return EntityDtoMapper.convertToDrugInteractionDTO(savedInteraction);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create drug interaction", e);
        }
    }

    public DrugInteractionDTO updateInteraction(int id, DrugInteractionDTO interactionDTO) {
        try {
            DrugInteraction existingInteraction = interactionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Drug interaction not found with id: " + id));

            // Update fields if provided
            if (interactionDTO.getSeverity() != null) {
                existingInteraction.setSeverity(interactionDTO.getSeverity());
            }
            if (interactionDTO.getDescription() != null && !interactionDTO.getDescription().trim().isEmpty()) {
                existingInteraction.setDescription(interactionDTO.getDescription().trim());
            }
            if (interactionDTO.getClinicalManagement() != null) {
                existingInteraction.setClinicalManagement(interactionDTO.getClinicalManagement());
            }
            if (interactionDTO.getEvidenceLevel() != null) {
                existingInteraction.setEvidenceLevel(interactionDTO.getEvidenceLevel());
            }

            // Products cannot be changed in an existing interaction
            if (interactionDTO.getProductId() != null || interactionDTO.getInteractsWithId() != null) {
                throw new GlobalException("Cannot change products in an existing interaction. Create a new interaction instead.");
            }

            DrugInteraction updatedInteraction = interactionRepository.save(existingInteraction);
            return EntityDtoMapper.convertToDrugInteractionDTO(updatedInteraction);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update drug interaction with id: " + id, e);
        }
    }

    public void deleteInteraction(int id) {
        try {
            if (!interactionRepository.existsById(id)) {
                throw new ResourceNotFoundException("Drug interaction not found with id: " + id);
            }
            interactionRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete drug interaction with id: " + id, e);
        }
    }
}