package pharmacy.pharmacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pharmacy.pharmacy.dao.UserRoleRepository;
import pharmacy.pharmacy.dao.UserRepository;
import pharmacy.pharmacy.dto.AuthRegisterResponseDTO;
import pharmacy.pharmacy.dto.AuthResponseDTO;
import pharmacy.pharmacy.dto.LoginDTO;
import pharmacy.pharmacy.dto.RegisterDTO;
import pharmacy.pharmacy.entity.ERole;
import pharmacy.pharmacy.entity.UserRole;
import pharmacy.pharmacy.entity.User;
import pharmacy.pharmacy.security.JwtUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API for user registration and login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Authenticate user", description = "Authenticates user credentials and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Parameter(description = "Login credentials", required = true)
            @RequestBody LoginDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }

    @Operation(summary = "Register new user", description = "Registers a new user with selected roles (defaults to ROLE_CUSTOMER if none provided)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(schema = @Schema(implementation = AuthRegisterResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - username/email already taken or invalid role")
    })

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Parameter(description = "User registration details", required = true)
            @RequestBody RegisterDTO signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());

        Set<UserRole> roles = new HashSet<>();

        if (signUpRequest.getRoles() == null || signUpRequest.getRoles().isEmpty()) {
            // Default role if none specified
            UserRole customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Customer role not found."));
            roles.add(customerRole);
        } else {
            // Process requested roles
            roles = signUpRequest.getRoles().stream()
                    .map(role -> roleRepository.findByName(ERole.valueOf(role)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            if (roles.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: None of the specified roles are valid!");
            }
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new AuthRegisterResponseDTO(user));
    }
}