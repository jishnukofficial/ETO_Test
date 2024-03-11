package com.todoapp.test.data.local




import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.todoapp.test.data.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<Todo>)
}
