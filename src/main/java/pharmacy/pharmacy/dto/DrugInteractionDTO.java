package pharmacy.pharmacy.dto;

import lombok.Data;
import pharmacy.pharmacy.enums.InteractionSeverity;

import java.util.Date;

@Data
public class DrugInteractionDTO {
    private Integer id;
    private Integer productId;
    private Integer interactsWithId;
    private InteractionSeverity severity;
    private String description;
    private String clinicalManagement;
    private String evidenceLevel;
    private Date createdAt;

    // Additional display fields
    private String productName;
    private String interactsWithName;
    private String productCategory;
    private String interactsWithCategory;
    private boolean severe;
}