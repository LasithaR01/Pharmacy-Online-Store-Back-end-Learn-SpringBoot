//package pharmacy.pharmacy.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import pharmacy.pharmacy.dao.UserRepository;
//import pharmacy.pharmacy.dto.UserDTO;
//import pharmacy.pharmacy.entity.User;
//import pharmacy.pharmacy.exception.UserSaveException;
//import pharmacy.pharmacy.exception.UserNotFoundException;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userDAO;
//
//    // Fetch all users
//    public List<User> getAllUsers() {
//        return userDAO.findAll();
//    }
//
//    // Fetch a user by ID and return ResponseEntity
//    public ResponseEntity<User> getUserById(UUID id) {
//        Optional<User> user = userDAO.findById(id);
//        return user.map(ResponseEntity::ok)
//                   .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Fetch a user by email
//    public Optional<User> getUserByEmail(String email) {
//        return Optional.ofNullable(userDAO.findByEmail(email));
//    }
//
//    // Save or update a user
//    public User saveUser(UserDTO userDTO) {
//        try {
//            User user = new User();
//            user.setName(userDTO.getName());
//            user.setEmail(userDTO.getEmail());
//            user.setContactNumber(userDTO.getContactNumber());
//            user.setRole(User.UserRole.valueOf(userDTO.getRole()));
//
//            return userDAO.save(user);
//
//        } catch (DataIntegrityViolationException ex) {
//            // Handle database constraint violations
//            throw new UserSaveException("Database error while saving user: " + ex.getMessage(), ex);
//        } catch (NullPointerException ex) {
//            // Handle specific null pointer exception
//            throw new UserSaveException("User or its details cannot be null", ex);
//        } catch (IllegalArgumentException ex) {
//            // Handle invalid enum values
//            throw new UserSaveException("Invalid role specified for user", ex);
//        } catch (Exception ex) {
//            // Catch other unexpected exceptions
//            throw new UserSaveException("An unexpected error occurred while saving the user", ex);
//        }
//    }
//
//    // Delete a user by ID
//    public void deleteUserById(UUID id) {
//        User user = userDAO.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
//        userDAO.delete(user);
//    }
//}
