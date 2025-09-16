package org.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public class TodoRequest {
    private String id;
    @Setter
    @Getter
    @NotNull
    @NotBlank
    private String text;
    @Getter
    @Setter
    private boolean done;

    public TodoRequest() {
    }

    public TodoRequest(String text, boolean done) {
        this.text = text;
        this.done = done;
    }
}