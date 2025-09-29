package com.neusoft.vo;
import java.util.Date;

public class User {
    private String username;
    private String email;
    private int employeeId;
    private String token;
    private String userType;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public int getEmployeeId() {
        return employeeId;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUserType() {
        return userType;
    }
}
