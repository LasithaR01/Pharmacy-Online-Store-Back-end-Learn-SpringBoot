package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // Enum for roles: Manager, Staff, etc.

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(nullable = false)
    private Double salary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

//    @ManyToOne
//    @JoinColumn(name = "branch_id", nullable = false)
//    private Branch branch;

    // Enum for role values
    public enum Role {
        Manager, Staff, Pharmacist
    }

    // Constructor
    public Employee() {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

//    public Branch getBranch() {
//        return branch;
//    }

//    public void setBranch(Branch branch) {
//        this.branch = branch;
//    }

    // PrePersist to initialize createdAt before saving to database
    @PrePersist
    public void prePersist() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}