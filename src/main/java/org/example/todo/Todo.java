package org.example.todo;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Todo {
    @Setter
    @Getter
    @Id
    private Long id;
    private String title;
    private boolean completed;


    public Todo(Object o, String buyMilk, boolean b) {
    }

    public Todo() {

    }

}
