package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dto.customer.CustomerCreateDTO;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "Endpoints for managing pharmacy customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<User> createCustomer(
            @Parameter(description = "Customer object to be created") @RequestBody CustomerCreateDTO customerCreateDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerCreateDTO));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<User> getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(customerService.getCustomerByPhoneNumber(phoneNumber));
    }
}
