

package com.todoapp.test.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoapp.test.data.local.TodoDao
import com.todoapp.test.data.model.Todo
import com.todoapp.test.data.remote.NetworkStatusChecker
import com.todoapp.test.data.remote.TodoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val todoService: TodoService,
    private val networkStatusChecker: NetworkStatusChecker
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodos(): Flow<List<Todo>> {
        return if (networkStatusChecker.isOnline()) {
            fetchTodosFromService()
        } else {
            fetchTodosFromLocal()
        }
    }

    private fun fetchTodosFromService(): Flow<List<Todo>> = flow {
        val todos = todoService.getTodos()
        // Insert fetched todos into the local database
        todoDao.insertAll(todos)
        emit(todos)
    }

    private fun fetchTodosFromLocal(): Flow<List<Todo>> = todoDao.getAllTodos()

    suspend fun updateTodoStatus(todo: Todo) {
        todoService.updateTodoStatus(todo)
    }
}

