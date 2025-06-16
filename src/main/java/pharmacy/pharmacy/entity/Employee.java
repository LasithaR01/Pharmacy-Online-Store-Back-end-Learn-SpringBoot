package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
               foreignKey = @ForeignKey(name = "fk_employee_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false,
               foreignKey = @ForeignKey(name = "fk_employee_branch"))
    private Branch branch;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(name = "hire_date", nullable = false)
    private Date hireDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Business logic methods
    public boolean isPharmacist() {
        return position != null && position.equalsIgnoreCase("Pharmacist");
    }

    public boolean isCashier() {
        return position != null && position.equalsIgnoreCase("Cashier");
    }
}