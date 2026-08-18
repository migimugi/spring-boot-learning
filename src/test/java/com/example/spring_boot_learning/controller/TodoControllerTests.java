package com.example.spring_boot_learning.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest // テストを実行するときにSpring Boot全体を準備する
@AutoConfigureMockMvc
class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTodosReturnsTodoList() throws Exception {
        mockMvc.perform(get("/todos"))
        .andExpect(status().isOk())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(
            jsonPath("$[0].title")
                .value("Spring Bootを学ぶ")
        );
    }

    @Test
    void getTodoReturnsTodo() throws Exception {
        mockMvc.perform(get("/todos/1"))
        .andExpect(status().isOk())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(
            jsonPath("$.title")
                .value("Spring Bootを学ぶ")
        )
        .andExpect(
            jsonPath("$.completed")
                .value(false)
        );
    }

    @Test
    void getTodoReturnsNotFoundWhenIdDoesNotExist() throws Exception {

        mockMvc.perform(get("/todos/999"))
        .andExpect(status().isNotFound()); // 404
    }

    @Test
    @DirtiesContext // テスト後、変更されたApplicationContextを再利用しない
    void createTodoReturnsCreatedTodo() throws Exception {
        mockMvc.perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "JUnitを学ぶ"
                    }
                        """)
        )
        .andExpect(status().isCreated()) // 201
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(
            jsonPath("$.title").value("JUnitを学ぶ")
        )
        .andExpect(
            jsonPath("$.completed").value(false)
        );

        mockMvc.perform(get("/todos/3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.title").value("JUnitを学ぶ"))
        .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createTodoReturnsBadRequestWhenTitleIsBlank() throws Exception {
        
        mockMvc.perform(
            post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "title": ""
                }
                """)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void updateTodoReturnsUpdatedTodo() throws Exception {
        mockMvc.perform(
            put("/todos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "title": "Spring Bootを復習する",
                    "completed" : true
                }
                """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(
            jsonPath("$.title")
            .value("Spring Bootを復習する")
        )
        .andExpect(
            jsonPath("$.completed").value(true)
        );

        mockMvc.perform(get("/todos/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Spring Bootを復習する"))
        .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @DirtiesContext
    void deleteTodoReturnsNoContentAndRemovesTodo() throws Exception {

        mockMvc.perform(delete("/todos/1"))
        .andExpect(status().isNoContent()); // 204

        mockMvc.perform(get("/todos/1"))
        .andExpect(status().isNotFound()); // 404
    }
}

// jsonPath()はJSON内の特定の場所を指定する: $はJSON全体 $.length()はJSON全体の配列要素数
// throw Exception は　「このメソッドからExceptionが出る可能性があります」という宣言 
// 実際に例外を発せさせるのは throw new