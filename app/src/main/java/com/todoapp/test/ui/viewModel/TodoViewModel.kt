
package com.todoapp.test.ui.viewModel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.test.data.model.Todo
import com.todoapp.test.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    init {
        fetchTodos()
    }

    fun updateTodoList() {
        fetchTodos()
    }

    private fun fetchTodos() {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                repository.getTodos().collect { todos ->
                    val headerPending = Todo(0, 0, "Pending", false, true)
                    val headerCompleted = Todo(0, 0, "Completed", true, true)
                    val updatedList = mutableListOf<Todo>()
                    updatedList.add(headerPending)
                    updatedList.addAll(todos.filter { !it.completed })
                    updatedList.add(headerCompleted)
                    updatedList.addAll(todos.filter { it.completed })
                    _todos.value = updatedList
                }
            }
        }
    }
}


