package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dao.UserRoleRepository;
import pharmacy.pharmacy.dto.customer.CustomerCreateDTO;
import pharmacy.pharmacy.entity.ERole;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.entity.UserRole;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    public User createCustomer(CustomerCreateDTO createDTO) {
        Set<UserRole> roles = new HashSet<>();
        UserRole customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Customer role not found."));
        roles.add(customerRole);

        User customer = new User();
        customer.setName(createDTO.getName());
        customer.setPhoneNumber(createDTO.getPhoneNumber());
        customer.setRoles(roles);
        return userRepository.save(customer);
    }

    public User getCustomerByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with phoneNumber: " + phoneNumber));
    }

}
