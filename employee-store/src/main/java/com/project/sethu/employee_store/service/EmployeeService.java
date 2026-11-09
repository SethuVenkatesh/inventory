package com.project.sethu.employee_store.service;

import com.project.sethu.employee_store.dto.EmployeeDTO;
import com.project.sethu.employee_store.entity.Employee;
import com.project.sethu.employee_store.exception.DuplicateResourceException;
import com.project.sethu.employee_store.exception.ResourceNotFoundException;
import com.project.sethu.employee_store.mapper.EmployeeMapper;
import com.project.sethu.employee_store.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public EmployeeDTO create(EmployeeDTO dto) {
        employeeRepository.findByEmailAndIsActiveTrue(dto.getEmail()).ifPresent(existing -> {
            throw new DuplicateResourceException(
                    "Employee with email '" + dto.getEmail() + "' already exists.");
        });
        Employee employee = new Employee();
        employeeMapper.toEntity(dto, employee);
        employee.setCreatedBy("system");
        employee.setUpdatedBy("system");
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDTO(saved);
    }

    public List<EmployeeDTO> getAll() {
        return employeeMapper.toDTOList(employeeRepository.findByIsActiveTrue());
    }

    public EmployeeDTO getById(String id) {
        Employee employee = employeeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        return employeeMapper.toDTO(employee);
    }

    public EmployeeDTO update(String id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        // Check duplicate email only if email is being changed
        if (dto.getEmail() != null && !dto.getEmail().equals(employee.getEmail())) {
            employeeRepository.findByEmailAndIsActiveTrue(dto.getEmail()).ifPresent(existing -> {
                throw new DuplicateResourceException(
                        "Employee with email '" + dto.getEmail() + "' already exists.");
            });
        }

        employeeMapper.toEntity(dto, employee);
        employee.setUpdatedBy("system");
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDTO(saved);
    }

    public void softDelete(String id) {
        Employee employee = employeeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        employee.setIsActive(false);
        employeeRepository.save(employee);
    }
}
