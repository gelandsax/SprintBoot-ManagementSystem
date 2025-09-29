package com.neusoft.dto;

public class ApproverChganeDto {
    public int getDept_id() {
        return dept_id;
    }

    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    private int dept_id;
    private AttendanceApprover Attendance;
    private VacationApprover Vacation;

    public void setAttendance(AttendanceApprover Attendance) {
        this.Attendance = Attendance;
    }
    public AttendanceApprover getAttendance() {
        return Attendance;
    }
    public void setVacation(VacationApprover Vacation) {
        this.Vacation = Vacation;
    }
    public VacationApprover getVacation() {
        return Vacation;
    }
}
