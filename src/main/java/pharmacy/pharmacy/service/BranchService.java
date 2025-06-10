package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.BranchRepository;
import pharmacy.pharmacy.dto.BranchDTO;
import pharmacy.pharmacy.entity.Branch;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

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

    public BranchDTO createBranch(BranchDTO branchDTO) {
        try {
            // Basic validation
            if (branchDTO.getName() == null || branchDTO.getName().isEmpty()) {
                throw new GlobalException("Branch name cannot be empty");
            }
            if (branchDTO.getLocation() == null || branchDTO.getLocation().isEmpty()) {
                throw new GlobalException("Branch location cannot be empty");
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
            Branch existingBranch = branchRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

            if (branchDTO.getName() != null) {
                existingBranch.setName(branchDTO.getName());
            }
            if (branchDTO.getLocation() != null) {
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
            if (!branchRepository.existsById(id)) {
                throw new ResourceNotFoundException("Branch not found with id: " + id);
            }
            branchRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete branch with id: " + id, e);
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
        branch.setName(dto.getName());
        branch.setLocation(dto.getLocation());
        branch.setContactNumber(dto.getContactNumber());
        branch.setOpeningHours(dto.getOpeningHours());
        return branch;
    }
}