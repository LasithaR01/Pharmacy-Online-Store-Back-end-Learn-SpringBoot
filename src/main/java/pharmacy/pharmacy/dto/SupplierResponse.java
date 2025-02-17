package pharmacy.pharmacy.dto;

import java.io.Serializable;
import java.util.UUID;

public class SupplierResponse implements Serializable {

    private UUID id;
    private String name;
    private String contactNumber;
    private String address;

    // Constructors
    public SupplierResponse() {}

    public SupplierResponse(UUID id, String name, String contactNumber, String address) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
