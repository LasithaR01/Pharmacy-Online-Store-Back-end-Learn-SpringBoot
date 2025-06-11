package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "branch")
@Getter
@Setter
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "opening_hours", columnDefinition = "TEXT")
    private String openingHours;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relationships
//    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Employee> employees = new HashSet<>();
//
//    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Inventory> inventories = new HashSet<>();
//
//    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Stock> stockEntries = new HashSet<>();
//
//    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Orders> orders = new HashSet<>();
//
//    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Alert> alerts = new HashSet<>();
//
//    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<RestockRequest> restockRequests = new HashSet<>();

    // Constructors
    public Branch() {}

    public Branch(String name, String location) {
        this.name = name;
        this.location = location;
    }
}