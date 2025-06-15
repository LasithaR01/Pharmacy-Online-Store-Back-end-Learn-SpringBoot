package pharmacy.pharmacy.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private String name;
    private String description;
    private Integer parentId;
}