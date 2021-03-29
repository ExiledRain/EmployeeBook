package io.exiled.employeebooking.controller;

import io.exiled.employeebooking.model.Employee;
import io.exiled.employeebooking.repository.EmployeeRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "Finds all Employees",
            notes = "If you want to see entire collection of contacts",
            response = List.class
    )
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
    @ApiOperation(value = "Finds Employee by specified ID",
            notes = "If you want to find a Single Employee by ID",
            response = Employee.class
    )
    public ResponseEntity<Employee> getEmployeeById(@ApiParam(value = "ID value for the Employee you need to retrieve", required = true)@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return new ResponseEntity<>(employeeOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/employees/active")
    @ApiOperation(value = "Finds all active Employees",
            notes = "If you want to see entire collection of Employees that have field 'active' =  true",
            response = Employee.class
    )
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
    @ApiOperation(value = "Create new Employee",
            notes = "Creates a specified Employee",
            response = Employee.class
    )
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
    @ApiOperation(value = "Edit and Employee",
            notes = "If you want to edit specific Employee found by defined ID",
            response = Employee.class
    )
    public ResponseEntity<Employee> updateEmployee(@ApiParam(value = "ID value for the Employee you need to update", required = true)@PathVariable Long id, @RequestBody Employee newEmployee) {
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
    @ApiOperation(value = "Deletes all Employees",
            notes = "If you want to delete all Employees"
    )
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
    @ApiOperation(value = "Delete specific Employee",
            notes = "If you want to delete specific Employee found by ID."
    )
    public ResponseEntity<Employee> deleteEmployeeById(@ApiParam(value = "ID value for the Employee you need to delete", required = true)@PathVariable Long id) {
        try {
            employeeRepository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
