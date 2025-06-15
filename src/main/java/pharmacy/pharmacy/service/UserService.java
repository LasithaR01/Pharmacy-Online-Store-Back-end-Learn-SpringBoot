package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.entity.UserRole;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dao.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                     UserRoleRepository userRoleRepository,
                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all users", e);
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(int id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve user with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve user with email: " + email, e);
        }
    }

    @Transactional
    public User createUser(User user) {
        try {
            validateUser(user);

            if (userRepository.existsByUsername(user.getUsername())) {
                throw new GlobalException("Username already exists", HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new GlobalException("Email already registered", HttpStatus.BAD_REQUEST);
            }

            // Encrypt password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return userRepository.save(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create user", e);
        }
    }

    @Transactional
    public User updateUser(int id, User userDetails) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

            if (userDetails.getUsername() != null && !user.getUsername().equals(userDetails.getUsername())) {
                if (userRepository.existsByUsername(userDetails.getUsername())) {
                    throw new GlobalException("Username already taken", HttpStatus.BAD_REQUEST);
                }
                user.setUsername(userDetails.getUsername());
            }

            if (userDetails.getEmail() != null && !user.getEmail().equals(userDetails.getEmail())) {
                if (userRepository.existsByEmail(userDetails.getEmail())) {
                    throw new GlobalException("Email already registered", HttpStatus.BAD_REQUEST);
                }
                user.setEmail(userDetails.getEmail());
            }

            if (userDetails.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            return userRepository.save(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update user with id: " + id, e);
        }
    }

    @Transactional
    public void deleteUser(int id) {
        try {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException("User not found with id: " + id);
            }
            userRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete user with id: " + id, e);
        }
    }

    @Transactional
    public User assignRoleToUser(int userId, int roleId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            UserRole role = userRoleRepository.findById((long) roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

            user.addRole(role);
            return userRepository.save(user);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to assign role to user with id: " + userId, e);
        }
    }

    @Transactional(readOnly = true)
    public boolean userExists(int id) {
        try {
            return userRepository.existsById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to check user existence with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String roleName) {
        try {
            return userRepository.findByRoles_Name(roleName);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve users with role: " + roleName, e);
        }
    }

    // Internal method for other services
    @Transactional(readOnly = true)
    public User getUserEntityById(int id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve user entity with id: " + id, e);
        }
    }

    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new GlobalException("Username cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new GlobalException("Email cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new GlobalException("Password cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (!isValidEmail(user.getEmail())) {
            throw new GlobalException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}