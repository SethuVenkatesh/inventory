package com.project.sethu.employee_store.controller;

import com.project.sethu.employee_store.dto.APIResponse;
import com.project.sethu.employee_store.dto.EmployeeDTO;
import com.project.sethu.employee_store.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Controller", description = "Controller for managing employee records")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create an employee", description = "Creates a new employee record.")
    public ResponseEntity<APIResponse<EmployeeDTO>> create(
            @Validated(EmployeeDTO.OnCreate.class) @RequestBody EmployeeDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(service.create(dto)));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all employees", description = "Returns a list of all active employees.")
    public ResponseEntity<APIResponse<List<EmployeeDTO>>> getAll() {
        return ResponseEntity.ok(APIResponse.success(service.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Returns the employee details for the given ID.")
    public ResponseEntity<APIResponse<EmployeeDTO>> getById(@PathVariable String id) {
        return ResponseEntity.ok(APIResponse.success(service.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee", description = "Updates the employee record for the given ID.")
    public ResponseEntity<APIResponse<EmployeeDTO>> update(
            @PathVariable String id,
            @Validated(EmployeeDTO.OnUpdate.class) @RequestBody EmployeeDTO dto
    ) {
        return ResponseEntity.ok(APIResponse.success(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee", description = "Soft-deletes the employee for the given ID.")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable String id) {
        service.softDelete(id);
        return ResponseEntity.ok(APIResponse.deleted());
    }
}
