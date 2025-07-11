package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pharmacy.pharmacy.enums.InteractionSeverity;

import java.util.Date;

@Entity
@Table(name = "drug_interaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_interaction_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interacts_with_id", nullable = false, foreignKey = @ForeignKey(name = "fk_interaction_with_product"))
    private Product interactsWith;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionSeverity severity;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "clinical_management", columnDefinition = "TEXT")
    private String clinicalManagement;

    @Column(name = "evidence_level")
    private String evidenceLevel;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Business logic methods
    public boolean isSevereInteraction() {
        return severity == InteractionSeverity.SEVERE;
    }

    public boolean involvesProduct(int productId) {
        return product.getId() == productId || interactsWith.getId() == productId;
    }
}