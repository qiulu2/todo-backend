package org.example.todo.controller;


import jakarta.validation.Valid;
import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.mapper.TodoMapper;
import org.example.todo.entity.Todo;
import org.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



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
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Todo updateTodo(@PathVariable String id, @Valid @RequestBody TodoRequest todoRequest) {
        Todo updatedTodo = TodoMapper.toTodo(todoRequest);
        updatedTodo.setId(id);
        todoService.updateTodo(id, updatedTodo);
        return todoService.getTodoById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Todo updateTodoDone(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        if (updates.containsKey("done")) {
            boolean done = (Boolean) updates.get("done");
            Todo existingTodo = todoService.getTodoById(id);
            existingTodo.setDone(done);
            todoService.updateTodo(id, existingTodo);
            return todoService.getTodoById(id);
        }
        throw new IllegalArgumentException("Invalid update request");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteTodoById(id);
    }

}
