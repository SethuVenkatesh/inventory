package com.project.sethu.employee_store.repository;

import com.project.sethu.employee_store.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    List<Employee> findByIsActiveTrue();

    Optional<Employee> findByIdAndIsActiveTrue(String id);

    Optional<Employee> findByEmailAndIsActiveTrue(String email);
}
