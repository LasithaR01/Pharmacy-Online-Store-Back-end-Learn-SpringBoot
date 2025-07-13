package pharmacy.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

@Service
public class CurrentUserService {
    @Autowired
    private UserRepository userRepository;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
