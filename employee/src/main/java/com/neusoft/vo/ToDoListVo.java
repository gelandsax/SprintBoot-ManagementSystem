package com.neusoft.vo;

import java.util.List;

public class ToDoListVo {
    private List<Todos> todos;

    public void setTodos(List<Todos> todos) {
        this.todos = todos;
    }
    public List<Todos> getTodos() {
        return todos;
    }
}
