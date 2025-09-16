package org.example.todo.service;

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

    public List<Todo> createTodo(Todo todo) {
        todo.setId(null);
        todoRepository.save(todo);
        return index();
    }

    public List<Todo> index() {
        return todoRepository.findAll();
    }

    public void updateTodo(String id, Todo updatedTodo) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id));
        existingTodo.setText(updatedTodo.getText());
        existingTodo.setDone(updatedTodo.isDone());
        todoRepository.save(existingTodo);
    }


    public Todo getTodoById(String id) {
        return todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id));
    }

    public void deleteTodoById(String id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
}
