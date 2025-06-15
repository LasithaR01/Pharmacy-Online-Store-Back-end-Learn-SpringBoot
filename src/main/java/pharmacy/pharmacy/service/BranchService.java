package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.BranchRepository;
import pharmacy.pharmacy.dto.BranchDTO;
import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Transactional(readOnly = true)
    public List<BranchDTO> getAllBranches() {
        try {
            return branchRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all branches", e);
        }
    }

    @Transactional(readOnly = true)
    public BranchDTO getBranchById(int id) {
        try {
            Branch branch = branchRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
            return convertToDTO(branch);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve branch with id: " + id, e);
        }
    }

    public Branch getBranchEntityById(int id) {
        try {
            return branchRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve branch entity with id: " + id, e);
        }
    }

    public BranchDTO createBranch(BranchDTO branchDTO) {
        try {
            // Basic validation
            if (branchDTO.getName() == null || branchDTO.getName().trim().isEmpty()) {
                throw new GlobalException("Branch name cannot be empty");
            }
            if (branchDTO.getLocation() == null || branchDTO.getLocation().trim().isEmpty()) {
                throw new GlobalException("Branch location cannot be empty");
            }

            // Check for duplicate branch name
            if (branchRepository.existsByName(branchDTO.getName())) {
                throw new GlobalException("Branch with this name already exists");
            }

            Branch branch = convertToEntity(branchDTO);
            Branch savedBranch = branchRepository.save(branch);
            return convertToDTO(savedBranch);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create branch", e);
        }
    }

    public BranchDTO updateBranch(int id, BranchDTO branchDTO) {
        try {
            Branch existingBranch = getBranchEntityById(id);

            if (branchDTO.getName() != null) {
                if (branchDTO.getName().trim().isEmpty()) {
                    throw new GlobalException("Branch name cannot be empty");
                }
                // Check if the new name is already used by another branch
                if (!existingBranch.getName().equals(branchDTO.getName()) &&
                    branchRepository.existsByName(branchDTO.getName())) {
                    throw new GlobalException("Another branch with this name already exists");
                }
                existingBranch.setName(branchDTO.getName());
            }
            if (branchDTO.getLocation() != null) {
                if (branchDTO.getLocation().trim().isEmpty()) {
                    throw new GlobalException("Branch location cannot be empty");
                }
                existingBranch.setLocation(branchDTO.getLocation());
            }
            if (branchDTO.getContactNumber() != null) {
                existingBranch.setContactNumber(branchDTO.getContactNumber());
            }
            if (branchDTO.getOpeningHours() != null) {
                existingBranch.setOpeningHours(branchDTO.getOpeningHours());
            }

            Branch updatedBranch = branchRepository.save(existingBranch);
            return convertToDTO(updatedBranch);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update branch with id: " + id, e);
        }
    }

    public void deleteBranch(int id) {
        try {
            Branch branch = getBranchEntityById(id);
            // Check if branch has any associated stock or other dependencies
            if (branchRepository.hasAssociatedStock(id)) {
                throw new GlobalException("Cannot delete branch with associated stock records");
            }
            branchRepository.delete(branch);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete branch with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public boolean branchExists(int id) {
        try {
            return branchRepository.existsById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to check if branch exists with id: " + id, e);
        }
    }

    private BranchDTO convertToDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setLocation(branch.getLocation());
        dto.setContactNumber(branch.getContactNumber());
        dto.setOpeningHours(branch.getOpeningHours());
        return dto;
    }

    private Branch convertToEntity(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setName(dto.getName().trim());
        branch.setLocation(dto.getLocation().trim());
        branch.setContactNumber(dto.getContactNumber());
        branch.setOpeningHours(dto.getOpeningHours());
        return branch;
    }
}