package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplier")
@Getter
@Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "tax_id", length = 50)
    private String taxId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stock> stockEntries = new HashSet<>();

    // Constructors
    public Supplier() {}

    public Supplier(String companyName, String address) {
        this.companyName = companyName;
        this.address = address;
    }

    // Business methods
    public void addStock(Stock stock) {
        stockEntries.add(stock);
        stock.setSupplier(this);
    }

    public void removeStock(Stock stock) {
        stockEntries.remove(stock);
        stock.setSupplier(null);
    }
}