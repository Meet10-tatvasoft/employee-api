package com.demo.dao;

import com.demo.model.Employee;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        Employee emp1 = new Employee();
        emp1.setId(1);
        emp1.setFirstName("John");
        emp1.setLastName("Doe");
        emp1.setEmail("john.doe@example.com");
        emp1.setDepartment("IT");
        emp1.setSalary(new BigDecimal("75000"));
        emp1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Employee emp2 = new Employee();
        emp2.setId(2);
        emp2.setFirstName("Alice");
        emp2.setLastName("Smith");
        emp2.setEmail("alice.smith@example.com");
        emp2.setDepartment("HR");
        emp2.setSalary(new BigDecimal("68000"));
        emp2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Employee emp3 = new Employee();
        emp3.setId(3);
        emp3.setFirstName("Bob");
        emp3.setLastName("Johnson");
        emp3.setEmail("bob.johnson@example.com");
        emp3.setDepartment("Finance");
        emp3.setSalary(new BigDecimal("82000"));
        emp3.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);

        return employees;
    }
}