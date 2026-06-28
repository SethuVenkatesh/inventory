package com.project.sethu.inventory_store.service;

import com.project.sethu.inventory_store.dto.AuthResponse;
import com.project.sethu.inventory_store.dto.LoginRequest;
import com.project.sethu.inventory_store.dto.RegisterRequest;
import com.project.sethu.inventory_store.entity.Organisation;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.entity.UserOrganisation;
import com.project.sethu.inventory_store.exception.DuplicateResourceException;
import com.project.sethu.inventory_store.exception.ResourceNotFoundException;
import com.project.sethu.inventory_store.repository.OrganisationRepository;
import com.project.sethu.inventory_store.repository.UserOrganisationRepository;
import com.project.sethu.inventory_store.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OrganisationRepository orgRepository;
    private final UserOrganisationRepository userOrganisationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository,
                       OrganisationRepository orgRepository,
                       UserOrganisationRepository userOrganisationRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.orgRepository = orgRepository;
        this.userOrganisationRepository = userOrganisationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public AuthResponse register(RegisterRequest request) {
        // 1. Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        // 2. Create default organisation from user's name
        Organisation org = new Organisation();
        org.setName("New Organisation");   // default name if not provided
        org.setIsActive(true);
        org.setCreatedBy(request.getEmail());
        org.setUpdatedBy(request.getEmail());
        Organisation savedOrg = orgRepository.save(org);

        // 3. Create user and link to organisation
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        User savedUser = userRepository.save(user);

        // 4. Link user to organisation as primary  ← key change
        UserOrganisation userOrg = new UserOrganisation();
        userOrg.setUser(savedUser);
        userOrg.setOrganisation(savedOrg);
        userOrg.setIsPrimary(true);// ← mark as primary
        userOrganisationRepository.save(userOrg);

        // 5. Generate token
        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getName(), user.getEmail(),
                user.getRole());
    }
}