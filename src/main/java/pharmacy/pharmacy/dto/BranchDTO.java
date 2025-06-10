package pharmacy.pharmacy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BranchDTO {
    private int id; // Added to match your UserDTO pattern
    private String name;
    private String location;
    private String contactNumber;
    private String openingHours;
}