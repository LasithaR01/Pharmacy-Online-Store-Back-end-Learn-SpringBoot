package pharmacy.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pharmacy.pharmacy.entity.ERole;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.entity.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String name;
    private ERole role;

    public MeDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.name = user.getName();
        this.role = user.getRoles().stream()
                .findFirst()
                .map(UserRole::getName)
                .orElse(null);
    }

}
