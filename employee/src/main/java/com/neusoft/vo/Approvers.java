package com.neusoft.vo;

public class Approvers {
    private int dept_id;
    private String dept;
    private String Attendance;
    private String Vacation;

    public void setEmployee_id(int dept_id) {
        this.dept_id = dept_id;
    }
    public int getEmployee_id() {
        return dept_id;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getDept() {
        return dept;
    }
    public void setAttendance(String Attendance) {
        this.Attendance = Attendance;
    }
    public String getAttendance() {
        return Attendance;
    }
    public void setVacation(String Vacation) {
        this.Vacation = Vacation;
    }
    public String getVacation() {
        return Vacation;
    }
}
