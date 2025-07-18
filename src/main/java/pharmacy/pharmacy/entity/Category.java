package pharmacy.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 2048)  // Increased length for URLs
    private String imageUrl;

    // Self-referential relationship for parent/child categories
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children;

    // Relationship with products
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    // Helper methods for bidirectional relationships
    public void addChildCategory(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    // Image handling convenience method
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl != null && !imageUrl.isBlank() ? imageUrl : null;
    }
}