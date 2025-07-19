package pharmacy.pharmacy.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CategoryCreateDTO {
    private String name;
    private String description;
    private MultipartFile imageFile;
    private Integer parentId;
}
