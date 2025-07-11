package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
<<<<<<< HEAD
            foreignKey = @ForeignKey(name = "fk_customer_user"))
=======
               foreignKey = @ForeignKey(name = "fk_customer_user"))
>>>>>>> origin/main
    private User user;

    @Column(nullable = false)
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "loyalty_points", nullable = false)
    private Integer loyaltyPoints = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Business logic methods
    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    public boolean canRedeemPoints(int points) {
        return points > 0 && this.loyaltyPoints >= points;
    }

    public void redeemPoints(int points) {
        if (canRedeemPoints(points)) {
            this.loyaltyPoints -= points;
        }
    }
}