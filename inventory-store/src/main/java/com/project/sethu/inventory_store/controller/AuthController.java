package com.project.sethu.inventory_store.controller;


import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.AuthResponse;
import com.project.sethu.inventory_store.dto.LoginRequest;
import com.project.sethu.inventory_store.dto.RegisterRequest;
import com.project.sethu.inventory_store.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Controller for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token for authentication.")
    public ResponseEntity<APIResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(authService.register(request)));
    }

    @PostMapping("/login")
    @Operation(summary = "Login a registered user", description = "Authenticates the user and returns a JWT token for subsequent API requests.")
    public ResponseEntity<APIResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(APIResponse.success(authService.login(request)));
    }
}