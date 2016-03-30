/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.trapasoft.jsf.beans;

import es.trapasoft.jsf.models.Employee;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author alejandro
 */
@ManagedBean
@SessionScoped
public class EmployeesBean {
    
    private List<Employee> employees = new ArrayList<Employee>();


    public EmployeesBean() {
    }

    @PostConstruct
    public void populateList() {
        for (int i =0; i < 100; i++) {
            Employee emp = new Employee();
            
            emp.setId(String.valueOf(i));
            emp.setName("Empleado # " +i );
            this.employees.add(emp);
        }
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    
}
