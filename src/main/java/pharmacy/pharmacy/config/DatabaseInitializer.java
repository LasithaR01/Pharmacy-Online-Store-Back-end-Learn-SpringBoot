package pharmacy.pharmacy.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pharmacy.pharmacy.entity.ERole;
import pharmacy.pharmacy.entity.UserRole;
import pharmacy.pharmacy.dao.UserRoleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRoleRepository roleRepository;

    public DatabaseInitializer(UserRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Get all existing roles in one query
        Set<ERole> existingRoles = roleRepository.findAll().stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());

        // Check which roles need to be created
        List<UserRole> rolesToCreate = Arrays.stream(ERole.values())
                .filter(role -> !existingRoles.contains(role))
                .map(role -> {
                    UserRole newRole = new UserRole();
                    newRole.setName(role);
                    return newRole;
                })
                .collect(Collectors.toList());

        // Save all missing roles in one batch
        if (!rolesToCreate.isEmpty()) {
            roleRepository.saveAll(rolesToCreate);
        }
    }
}