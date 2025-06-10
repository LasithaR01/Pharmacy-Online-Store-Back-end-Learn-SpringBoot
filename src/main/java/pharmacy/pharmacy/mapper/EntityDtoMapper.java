// EntityDtoMapper.java
package pharmacy.pharmacy.mapper;

import pharmacy.pharmacy.dto.BranchDTO;
import pharmacy.pharmacy.entity.Branch;

public class EntityDtoMapper {

    public static BranchDTO convertToBranchDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setName(branch.getName());
        dto.setLocation(branch.getLocation());
        dto.setContactNumber(branch.getContactNumber());
        dto.setOpeningHours(branch.getOpeningHours());
        return dto;
    }

    public static Branch convertToBranch(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setName(dto.getName());
        branch.setLocation(dto.getLocation());
        branch.setContactNumber(dto.getContactNumber());
        branch.setOpeningHours(dto.getOpeningHours());
        return branch;
    }
}