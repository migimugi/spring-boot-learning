package com.example.spring_boot_learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import com.example.spring_boot_learning.dto.CreateTodoRequest;
import com.example.spring_boot_learning.dto.TodoResponse;
import com.example.spring_boot_learning.service.TodoService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import com.example.spring_boot_learning.dto.UpdateTodoRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public List<TodoResponse> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping("/todos/{id}")
    public TodoResponse getTodo(
        @PathVariable(name = "id") long id
    ){
        return todoService.getTodo(id);
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse createTodo(
        @Valid
        @RequestBody 
        CreateTodoRequest request
    ){
        return todoService.createTodo(request);
    }

    @PutMapping("/todos/{id}")
    public TodoResponse updateTodo(
        @PathVariable(name = "id") long id,
        @Valid @RequestBody UpdateTodoRequest request
    ) {
        return todoService.updateTodo(id, request);
    }

    @DeleteMapping("/todos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(
        @PathVariable(name = "id") long id) {
            todoService.deleteTodo(id);
        }
}
