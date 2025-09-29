package com.neusoft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.mapper.AttendanceRetroactiveMapper;
import com.neusoft.mapper.UsersMapper;
import com.neusoft.mapper.VacationRequestsMapper;
import com.neusoft.mapper.WorkCalendarMapper;
import com.neusoft.model.AttendanceRetroactive;
import com.neusoft.model.Users;
import com.neusoft.model.VacationRequests;
import com.neusoft.model.WorkCalendar;
import com.neusoft.service.IToDOListService;
import com.neusoft.utils.UserUtils;
import com.neusoft.vo.ToDoListVo;
import com.neusoft.vo.Todos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoListServiceImpl implements IToDOListService {
    @Resource
    AttendanceRetroactiveMapper attendanceRetroactiveMapper;
    @Resource
    VacationRequestsMapper vacationRequestsMapper;
    @Resource
    WorkCalendarMapper workCalendarMapper;
    @Autowired
    UsersMapper usersMapper;
    public Todos ChangePwdToDo(){
        UserUtils userUtils = new UserUtils();
        Users LoginUser = userUtils.getLoginUser();
        if(LoginUser != null){
            if(LoginUser.getStatus().equals("inactive")){
                Todos todos = new Todos();
                todos.setCode("Change_Password");
                todos.setCount(1);
                return todos;
            }
        }
        return null;
    }

    public Todos getAttendanceApproveTodo(){
        QueryWrapper<AttendanceRetroactive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","未审批");
        int count = attendanceRetroactiveMapper.selectCount(queryWrapper);
        if(count != 0){
            Todos todos = new Todos();
            todos.setCode("Attendance_Approve");
            todos.setCount(count);
            return todos;
        }
        return null;
    }

    public Todos getVacationApproveTodo(){
        QueryWrapper<VacationRequests> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","未审批");
        int count = vacationRequestsMapper.selectCount(queryWrapper);
        if(count != 0){
            Todos todos = new Todos();
            todos.setCode("Vacation_Approve");
            todos.setCount(count);
            return todos;
        }
        return null;
    }

    public Todos InitWorkCalendarTodo(){
        int nextYear = LocalDate.now().plusYears(1).getYear();
        QueryWrapper<WorkCalendar> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("EXTRACT(YEAR FROM work_date) = {0}", nextYear);
        int count = workCalendarMapper.selectCount(queryWrapper);

        if(count == 0){
            Todos todos = new Todos();
            todos.setCode("Init_WorkCalendar");
            todos.setCount(1);   // 提示一次即可
            return todos;
        }
        return null;
    }

    public ToDoListVo getAllTodo(){
        List<Todos> todosList = new ArrayList<>();
        UserUtils userUtils = new UserUtils();
        Users LoginUser = userUtils.getLoginUser();
        List<String> permissions = usersMapper.getPermissionsByUserId(LoginUser.getId());

        // 修改密码提示
        Todos changePwd = ChangePwdToDo();
        if (changePwd != null) {
            todosList.add(changePwd);
        }

        // 请假审批
        if (permissions.contains("假期审批")) {
            Todos vacationApprove = getVacationApproveTodo();
            if (vacationApprove != null) {
                todosList.add(vacationApprove);
            }
        }

        // 考勤审批
        if (permissions.contains("考勤审批")) {
            Todos attendanceApprove = getAttendanceApproveTodo();
            if (attendanceApprove != null) {
                todosList.add(attendanceApprove);
            }
        }

        // 初始化工作日历
        if (permissions.contains("管理员")) {
            Todos initWorkCalendar = InitWorkCalendarTodo();
            if (initWorkCalendar != null) {
                todosList.add(initWorkCalendar);
            }
        }

        ToDoListVo toDoListVo = new ToDoListVo();
        toDoListVo.setTodos(todosList);
        return toDoListVo;
    }

}
