package com.todoapp.test.data.remote


import com.todoapp.test.data.model.Todo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface TodoService {
    @GET("/todos")
    suspend fun getTodos(): List<Todo>

    @PUT("/todos/{id}")
    suspend fun updateTodoStatus(@Body todo: Todo)
}
