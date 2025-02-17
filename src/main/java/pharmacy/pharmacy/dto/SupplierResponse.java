package pharmacy.pharmacy.dto;

import java.util.UUID;
import java.util.Date;

public class SupplierResponse {

    private UUID id;
    private String name;
    private String contactNumber;
    private String address;
    private Date createdAt;

    // Constructors
    public SupplierResponse() {}

    public SupplierResponse(UUID id, String name, String contactNumber, String address, Date createdAt) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.address = address;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
