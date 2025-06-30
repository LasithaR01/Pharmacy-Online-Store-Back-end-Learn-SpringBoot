package pharmacy.pharmacy.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {
    private Integer id;
    private Integer userId;
    private String address;
    private Date dateOfBirth;
    private Integer loyaltyPoints;
    private Date createdAt;

    // Additional display fields
    private String userName;
    private String userEmail;
    private String userContactNumber;
}