package org.example.todo;

import org.example.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

interface TodoRepository extends JpaRepository<Todo, Integer> {

}
