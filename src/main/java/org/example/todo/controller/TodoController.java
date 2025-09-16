package org.example.todo.controller;


import jakarta.validation.Valid;
import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.TodoResponse;
import org.example.todo.dto.mapper.TodoMapper;
import org.example.todo.entity.Todo;
import org.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    List<Todo> index() {
        return todoService.index();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    List<Todo> create(@Valid @RequestBody TodoRequest todoRequest) {
        Todo todo = TodoMapper.toTodo(todoRequest);
        Todo createdTodo = todoService.createTodo(todo);
        return todoService.index();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoResponse updateTodo(@PathVariable int id, @Valid @RequestBody TodoRequest todoRequest) {
        Todo updatedTodo = TodoMapper.toTodo(todoRequest);
        todoService.updateTodo(id, updatedTodo);
        return null;
    }

}
