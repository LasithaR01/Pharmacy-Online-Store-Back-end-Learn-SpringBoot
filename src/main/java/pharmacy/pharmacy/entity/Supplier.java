package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Use AUTO for UUID
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15)
    private String contactNumber;

    @Column(nullable = false, length = 255)
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    // Relationship with SupplyOrder (One supplier can have multiple supply orders)
//    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SupplyOrder> supplyOrders;
//
//    // Relationship with Stock (One supplier can provide multiple stocks)
//    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Stock> stocks;

    // Constructors
    public Supplier() {
        this.id = UUID.randomUUID(); // Generate UUID when creating a new supplier
    }

    public Supplier(String name, String contactNumber, String address, Date createdAt) {
        this.id = UUID.randomUUID();
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

//    public List<SupplyOrder> getSupplyOrders() {
//        return supplyOrders;
//    }
//
//    public void setSupplyOrders(List<SupplyOrder> supplyOrders) {
//        this.supplyOrders = supplyOrders;
//    }
//
//    public List<Stock> getStocks() {
//        return stocks;
//    }
//
//    public void setStocks(List<Stock> stocks) {
//        this.stocks = stocks;
//    }
}
