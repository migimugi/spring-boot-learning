package com.example.spring_boot_learning.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import com.example.spring_boot_learning.dto.CreateTodoRequest;

import com.example.spring_boot_learning.dto.TodoResponse;
import com.example.spring_boot_learning.dto.UpdateTodoRequest;
import org.springframework.data.domain.Sort;

import com.example.spring_boot_learning.entity.Todo;
import com.example.spring_boot_learning.repository.TodoRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(
        TodoRepository todoRepository
    ) {
        this.todoRepository = todoRepository;
    }


    public List<TodoResponse> getTodos() {
        List<Todo> todoEntities = todoRepository.findAll(Sort.by("id"));

        List<TodoResponse> responses = new ArrayList<>();

        for (Todo todo : todoEntities) {
            responses.add(toResponse(todo));
        }

        return responses;
    }


    public TodoResponse getTodo(long id) {
        Todo todo = findTodo(id);

        return toResponse(todo);
    }

    public TodoResponse createTodo(
        CreateTodoRequest request
    ) {
        Todo todo = new Todo(request.title());  // idはまだnull

        Todo savedTodo = todoRepository.save(todo); // HibernateがINSERTを実行し、H2がIDを自動採番

        return toResponse(savedTodo);
    }


    public TodoResponse updateTodo(
        long id, 
        UpdateTodoRequest request
    ) {
        Todo todo = findTodo(id);

        todo.update(request.title(), request.completed());

        Todo savedTodo = todoRepository.save(todo);

        return toResponse(savedTodo);
    }

    public void deleteTodo(long id) {
        Todo todo = findTodo(id);
        
        todoRepository.delete(todo); 
        // Javaではインスタンスのフィールドを使うとき名前が曖昧でなければthis.を省略できる
    }

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
            todo.getId(),
            todo.getTitle(),
            todo.isCompleted()
        );
    }

    private Todo findTodo(long id) {
        return todoRepository.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todo not found"
            )
        );
    }
}