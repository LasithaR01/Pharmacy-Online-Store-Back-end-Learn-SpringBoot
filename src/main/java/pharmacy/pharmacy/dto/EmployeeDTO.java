package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.EmployeePosition;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmployeeDTO {
    private Integer id;
    private Integer userId;
    private Integer branchId;
    private String position;
    private BigDecimal salary;
    private Date hireDate;
    private Date createdAt;

    // Additional display fields
    private String name;
    private String email;
    private String username;
    private String phoneNumber;
    private String branchName;
    private String branchLocation;
}