package pharmacy.pharmacy.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmployeeCreateRequest {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "PhoneNumber is required")
    private String phoneNumber;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "BranchId is required")
    private Integer branchId;

    @NotNull(message = "Position is required")
    private String position;

    @NotNull(message = "Salary is required")
    private BigDecimal salary;

    @NotNull(message = "Hire Date is required")
    private Date hireDate;

}
