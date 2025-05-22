package pharmacy.pharmacy.mapper;

import pharmacy.pharmacy.dto.SupplyOrderRequestDTO;
import pharmacy.pharmacy.dto.SupplyOrderResponseDTO;
import pharmacy.pharmacy.entity.SupplyOrder;

public class SupplyOrderMapper {

    public static SupplyOrder toEntity(SupplyOrderRequestDTO dto) {
        SupplyOrder supplyOrder = new SupplyOrder();
        supplyOrder.setOrderDate(dto.getOrderDate());
//        supplyOrder.setStatus(dto.getStatus());
        return supplyOrder;
    }

    public static SupplyOrderResponseDTO toResponseDTO(SupplyOrder supplyOrder) {
        SupplyOrderResponseDTO responseDTO = new SupplyOrderResponseDTO();
        responseDTO.setId(supplyOrder.getId());
        responseDTO.setSupplierName(supplyOrder.getSupplier().getName());
//        responseDTO.setBranchName(supplyOrder.getBranch().getName());
        responseDTO.setOrderDate(supplyOrder.getOrderDate());
//        responseDTO.setStatus(supplyOrder.getStatus());
        return responseDTO;
    }
}
