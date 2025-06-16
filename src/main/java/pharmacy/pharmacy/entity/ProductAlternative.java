package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "product_alternative")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAlternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_alternative_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alternative_product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_alternative_alt_product"))
    private Product alternativeProduct;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Business logic methods
    public boolean isSameCategory() {
        return product.getCategory().equals(alternativeProduct.getCategory());
    }

    public boolean isCheaperAlternative() {
        return alternativeProduct.getPrice().compareTo(product.getPrice()) < 0;
    }

    public boolean isInStock() {
        return alternativeProduct.getStockQuantity() > 0;
    }
}