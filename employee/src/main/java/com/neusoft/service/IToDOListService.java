package com.neusoft.service;

import com.neusoft.vo.ToDoListVo;
import com.neusoft.vo.Todos;

public interface IToDOListService {
    Todos ChangePwdToDo();
    Todos getAttendanceApproveTodo();
    Todos getVacationApproveTodo();
    Todos InitWorkCalendarTodo();
    ToDoListVo getAllTodo();
}
