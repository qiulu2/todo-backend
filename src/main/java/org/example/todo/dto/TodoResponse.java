package org.example.todo.dto;


import org.example.todo.entity.Todo;

import java.util.List;

public class TodoResponse {
    List<Todo> todos;

    public TodoResponse(List<Todo> todos) {
        this.todos = todos;
    }

}
