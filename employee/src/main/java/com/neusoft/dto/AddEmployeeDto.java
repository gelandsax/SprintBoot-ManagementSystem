package com.neusoft.dto;

public class AddEmployeeDto {
    private Employee employee;
    private UserLogin user;

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setUser(UserLogin user) {
        this.user = user;
    }
    public UserLogin getUser() {
        return user;
    }
}
