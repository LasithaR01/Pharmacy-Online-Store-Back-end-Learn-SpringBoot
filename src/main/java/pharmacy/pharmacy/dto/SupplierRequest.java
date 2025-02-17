package pharmacy.pharmacy.dto;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class SupplierRequest implements Serializable {

//    @NotBlank(message = "Name cannot be empty")
//    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

//    @NotBlank(message = "Contact number cannot be empty")
//    @Pattern(regexp = "\\d{10,15}", message = "Contact number must be between 10-15 digits")
    private String contactNumber;

//    @NotBlank(message = "Address cannot be empty")
//    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    // Constructors
    public SupplierRequest() {}

    public SupplierRequest(String name, String contactNumber, String address) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
