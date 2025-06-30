package pharmacy.pharmacy.service;

import io.sentry.Sentry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacy.pharmacy.dao.CustomerRepository;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dto.CustomerDTO;
import pharmacy.pharmacy.entity.Customer;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.exception.GlobalException;
import pharmacy.pharmacy.exception.ResourceNotFoundException;
import pharmacy.pharmacy.mapper.EntityDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository,
                         UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        try {
            return customerRepository.findAll().stream()
                    .map(EntityDtoMapper::convertToCustomerDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve all customers", e);
        }
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(int id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
            return EntityDtoMapper.convertToCustomerDTO(customer);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve customer with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByUserId(int userId) {
        try {
            Customer customer = customerRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));
            return EntityDtoMapper.convertToCustomerDTO(customer);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve customer for user id: " + userId, e);
        }
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        try {
            // Validate required fields
            if (customerDTO.getUserId() == null) {
                throw new GlobalException("User ID must be specified");
            }
            if (customerDTO.getAddress() == null || customerDTO.getAddress().trim().isEmpty()) {
                throw new GlobalException("Address cannot be empty");
            }
            if (customerDTO.getDateOfBirth() == null) {
                throw new GlobalException("Date of birth must be specified");
            }

            // Check if user exists and is not already a customer
            User user = userRepository.findById(customerDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + customerDTO.getUserId()));

            if (customerRepository.existsByUserId(user.getId())) {
                throw new GlobalException("User is already registered as a customer");
            }

            // Create new customer
            Customer customer = Customer.builder()
                    .user(user)
                    .address(customerDTO.getAddress().trim())
                    .dateOfBirth(customerDTO.getDateOfBirth())
                    .loyaltyPoints(customerDTO.getLoyaltyPoints() != null ? customerDTO.getLoyaltyPoints() : 0)
                    .build();

            Customer savedCustomer = customerRepository.save(customer);
            return EntityDtoMapper.convertToCustomerDTO(savedCustomer);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to create customer", e);
        }
    }

    public CustomerDTO updateCustomer(int id, CustomerDTO customerDTO) {
        try {
            Customer existingCustomer = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

            // Update fields if provided
            if (customerDTO.getAddress() != null && !customerDTO.getAddress().trim().isEmpty()) {
                existingCustomer.setAddress(customerDTO.getAddress().trim());
            }
            if (customerDTO.getDateOfBirth() != null) {
                existingCustomer.setDateOfBirth(customerDTO.getDateOfBirth());
            }
            if (customerDTO.getLoyaltyPoints() != null && customerDTO.getLoyaltyPoints() >= 0) {
                existingCustomer.setLoyaltyPoints(customerDTO.getLoyaltyPoints());
            }

            // User cannot be changed in an existing customer record
            if (customerDTO.getUserId() != null) {
                throw new GlobalException("Cannot change user association for an existing customer");
            }

            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return EntityDtoMapper.convertToCustomerDTO(updatedCustomer);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to update customer with id: " + id, e);
        }
    }

    public void deleteCustomer(int id) {
        try {
            if (!customerRepository.existsById(id)) {
                throw new ResourceNotFoundException("Customer not found with id: " + id);
            }
            customerRepository.deleteById(id);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to delete customer with id: " + id, e);
        }
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByLoyaltyPointsRange(int minPoints, int maxPoints) {
        try {
            return customerRepository.findByLoyaltyPointsRange(minPoints, maxPoints).stream()
                    .map(EntityDtoMapper::convertToCustomerDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to retrieve customers by loyalty points range", e);
        }
    }

    public CustomerDTO addLoyaltyPoints(int customerId, int points) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

            customer.addLoyaltyPoints(points);
            Customer updatedCustomer = customerRepository.save(customer);
            return EntityDtoMapper.convertToCustomerDTO(updatedCustomer);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to add loyalty points to customer with id: " + customerId, e);
        }
    }

    public CustomerDTO redeemLoyaltyPoints(int customerId, int points) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

            if (!customer.canRedeemPoints(points)) {
                throw new GlobalException("Customer doesn't have enough points to redeem");
            }

            customer.redeemPoints(points);
            Customer updatedCustomer = customerRepository.save(customer);
            return EntityDtoMapper.convertToCustomerDTO(updatedCustomer);
        } catch (Exception e) {
            Sentry.captureException(e);
            throw new GlobalException("Failed to redeem loyalty points for customer with id: " + customerId, e);
        }
    }
}