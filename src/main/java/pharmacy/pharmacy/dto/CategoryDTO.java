package pharmacy.pharmacy.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Integer id;  // Added ID field
    private String name;
    private String description;
    private String imageUrl;  // Added image URL field
    private Integer parentId;

    // For hierarchical representation
    private List<CategoryDTO> children;  // Added children list

    // For product count (optional)
    private Integer productCount;
}