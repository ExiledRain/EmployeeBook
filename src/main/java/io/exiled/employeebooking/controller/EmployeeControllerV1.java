package io.exiled.employeebooking.controller;

import io.exiled.employeebooking.model.Employee;
import io.exiled.employeebooking.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller to work with Employee record in database.
 *
 * @author Vassili Moskaljov
 * @version 1.0
 */
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EmployeeControllerV1 {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeControllerV1(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(required = false) String firstNameFilter) {
        try {
            List<Employee> employeeList = new ArrayList<>();
            if (firstNameFilter == null || firstNameFilter.isEmpty()) {
                employeeList.addAll(employeeRepository.findAll());
            } else {
                employeeList.addAll(employeeRepository.findByFirstNameContaining(firstNameFilter));
            }

            return new ResponseEntity<>(employeeList, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return new ResponseEntity<>(employeeOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/employees/active")
    public ResponseEntity<List<Employee>> findByActive() {
        try {
            List<Employee> activeEmployees = employeeRepository.findByActive(true);
            if (activeEmployees.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(activeEmployees, HttpStatus.OK);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee newEmployee) {
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(newEmployee, employee, "id");
            return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            BeanUtils.copyProperties(newEmployee, employee, "id");
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/employees")
    public ResponseEntity<Employee> deleteAllEmployees() {
        try {
            employeeRepository.deleteAll();
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Long id) {
        try {
            employeeRepository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
