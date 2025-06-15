package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.PrescriptionStatus;

import java.util.Date;

@Data
public class PrescriptionDTO {
    private Integer id;
    private Integer userId;
    private String doctorName;
    private String doctorContact;
    private Date prescriptionDate;
    private PrescriptionStatus status;
    private String notes;
    private String documentUrl;
    private Integer approvedById;
    private Date createdAt;
    private Date approvedAt;

    // Additional display fields
    private String userName;
    private String approvedByName;
}