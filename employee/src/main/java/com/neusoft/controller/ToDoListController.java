package com.neusoft.controller;

import com.neusoft.response.ApiResponse;
import com.neusoft.service.IToDOListService;
import com.neusoft.vo.ToDoListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToDoListController {
    @Autowired
    IToDOListService toDoListService;

    @GetMapping("/api/ToDoList")
    public ApiResponse<ToDoListVo> getToDoList(){
        ToDoListVo toDoListVo = toDoListService.getAllTodo();
        return ApiResponse.success(toDoListVo);
    }
}
