package com.demo.dao;

import com.demo.db.DBConnection;
import com.demo.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() throws SQLException {

        String sql = "SELECT * FROM employees ORDER BY id";
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        List<Employee> employees = new ArrayList<>();

        while (rs.next()) {

            Employee employee = new Employee();

            employee.setId(rs.getInt("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setEmail(rs.getString("email"));
            employee.setDepartment(rs.getString("department"));
            employee.setSalary(rs.getBigDecimal("salary"));
            employee.setCreatedAt(rs.getTimestamp("created_at"));

            employees.add(employee);
        }

        rs.close();
        statement.close();
        return employees;
    }

    public Employee getEmployeeById(int id) throws SQLException {

        String sql = "SELECT * FROM employees WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {

            Employee employee = new Employee();

            employee.setId(rs.getInt("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setEmail(rs.getString("email"));
            employee.setDepartment(rs.getString("department"));
            employee.setSalary(rs.getBigDecimal("salary"));
            employee.setCreatedAt(rs.getTimestamp("created_at"));
            return employee;
        }
        return null;
    }

    public void addEmployee(Employee employee) throws SQLException {

        String sql = "INSERT INTO employees (first_name,last_name,email,department,salary) VALUES (?,?,?,?,?)";

        Connection connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, employee.getFirstName());
        statement.setString(2, employee.getLastName());
        statement.setString(3, employee.getEmail());
        statement.setString(4, employee.getDepartment());
        statement.setBigDecimal(5, employee.getSalary());

        statement.executeUpdate();

        statement.close();
    }

    public boolean updateEmployee(int id, Employee employee) throws SQLException {

        Employee isEmployee = this.getEmployeeById(id);
        if(isEmployee==null){
            return false;
        }

        String sql = "UPDATE employees SET first_name=?,last_name=?,email=?,department=?,salary=? WHERE id=?";

        Connection connection = DBConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, employee.getFirstName());
        statement.setString(2, employee.getLastName());
        statement.setString(3, employee.getEmail());
        statement.setString(4, employee.getDepartment());
        statement.setBigDecimal(5, employee.getSalary());
        statement.setInt(6, id);

        statement.executeUpdate();

        statement.close();
        return true;
    }

    public boolean deleteEmployee(int id) throws SQLException {

        Employee isEmployee = this.getEmployeeById(id);
        if(isEmployee==null){
            return false;
        }

        String sql = "DELETE FROM employees WHERE id=?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        statement.executeUpdate();
        statement.close();
        return true;
    }
}