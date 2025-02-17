package pharmacy.pharmacy.dto;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;

public class SupplierRequest {

//    @NotBlank(message = "Supplier name is required")
//    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

//    @NotBlank(message = "Contact number is required")
//    @Size(max = 15, message = "Contact number cannot exceed 15 characters")
    private String contactNumber;

//    @NotBlank(message = "Address is required")
//    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    // Constructors
    public SupplierRequest() {}

    public SupplierRequest(String name, String contactNumber, String address) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
