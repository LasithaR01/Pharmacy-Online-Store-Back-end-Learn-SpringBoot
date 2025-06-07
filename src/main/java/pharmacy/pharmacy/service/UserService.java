package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.entity.UserRole;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dao.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all users", e);
        }
    }

    public User getUserById(int id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new GlobalException("User not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve user with id: " + id, e);
        }
    }

    public User createUser(User user) {
        try {
            // Basic validation
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new GlobalException("Username cannot be empty");
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new GlobalException("Email cannot be empty");
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new GlobalException("Password cannot be empty");
            }

            // Check if username or email already exists
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new GlobalException("Username already exists");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new GlobalException("Email already registered");
            }

            return userRepository.save(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create user", e);
        }
    }

    public User updateUser(int id, User userDetails) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new GlobalException("User not found with id: " + id));

            if (userDetails.getUsername() != null) {
                // Check if new username is already taken by another user
                if (!user.getUsername().equals(userDetails.getUsername()) &&
                    userRepository.existsByUsername(userDetails.getUsername())) {
                    throw new GlobalException("Username already taken");
                }
                user.setUsername(userDetails.getUsername());
            }

            if (userDetails.getEmail() != null) {
                // Check if new email is already registered by another user
                if (!user.getEmail().equals(userDetails.getEmail()) &&
                    userRepository.existsByEmail(userDetails.getEmail())) {
                    throw new GlobalException("Email already registered");
                }
                user.setEmail(userDetails.getEmail());
            }

            if (userDetails.getPassword() != null) {
                user.setPassword(userDetails.getPassword());
            }

            return userRepository.save(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update user with id: " + id, e);
        }
    }

    public void deleteUser(int id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new GlobalException("User not found with id: " + id));
            userRepository.delete(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete user with id: " + id, e);
        }
    }

    public User assignRoleToUser(int userId, UserRole role) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new GlobalException("User not found with id: " + userId));

            // Check if role exists
            Optional<UserRole> existingRole = userRoleRepository.findByName(role.getName());
            if (existingRole.isEmpty()) {
                throw new GlobalException("Role not found: " + role.getName());
            }

            user.addRole(existingRole.get());
            return userRepository.save(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to assign role to user with id: " + userId, e);
        }
    }
}