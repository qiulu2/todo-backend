package org.example.todo.service;

import org.example.todo.dto.TodoResponse;
import org.example.todo.dto.mapper.TodoMapper;
import org.example.todo.entity.Todo;
import org.example.todo.exception.TodoNotFoundException;
import org.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(Todo todo) {
        todo.setId(null);
        return todoRepository.save(todo);
    }

    public List<Todo> index() {
        return todoRepository.findAll();
    }

    public void updateTodo(String id, Todo updatedTodo) {
        Todo existingTodo = todoRepository.findById(Integer.parseInt(id)).orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id));
        existingTodo.setText(updatedTodo.getText());
        existingTodo.setDone(updatedTodo.isDone());
        todoRepository.save(existingTodo);
    }
}
