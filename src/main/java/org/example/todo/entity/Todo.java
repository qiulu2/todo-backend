package org.example.todo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class Todo {

    @Id
    private int id;

    private String text;
    private boolean done;

    public Todo() {

    }

    public Todo(int id, String text, boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }

    public Todo(String text, boolean done) {
        this.text = text;
        this.done = done;
    }

    @PrePersist
    public void ensureId() {
        if (this.id == 0) {
            this.id = UUID.randomUUID().hashCode();
        }
    }
}
