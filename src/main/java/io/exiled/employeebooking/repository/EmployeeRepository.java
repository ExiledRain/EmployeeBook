package io.exiled.employeebooking.repository;

import io.exiled.employeebooking.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Employee} class.
 *
 * @author Vassili Moskaljov
 * @version 1.0
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * Method to retrieve all records that have active==true
     *
     * @param active criteria for search.
     * @return List of Employee where field active is true.
     */
    List<Employee> findByActive(boolean active);

    /**
     * Method to retrieve all records that contain @param firstName in the FirstName of Employee.
     *
     * @param firstNameFilter criteria for search.
     * @return List of Employee where FirstName contains firstName.
     * */
    List<Employee> findByFirstNameContaining(String firstNameFilter);
}
