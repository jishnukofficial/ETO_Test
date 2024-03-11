package com.todoapp.test.data.remote

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.todoapp.test.data.local.AppDatabase
import com.todoapp.test.data.local.TodoDao
import com.todoapp.test.data.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "todo-db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(database: AppDatabase): TodoDao {
        return database.todoDao()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton

    @Provides
    fun provideNetworkStatusChecker(@ApplicationContext context: Context): NetworkStatusChecker {
        return NetworkStatusChecker(context)
    }
    @Provides
    fun provideNetworkChangeReceiver(@ApplicationContext context: Context): (Boolean) -> Unit {
        return { isConnected ->
            // Handle network status change here
            // For example, you can update UI, show toast, etc.
            if (isConnected) {
                // Connected to network
                // Perform actions accordingly
            } else {
                // Disconnected from network
                // Perform actions accordingly
            }
        }
    }

    @Singleton
    @Provides
    fun provideTodoRepository(todoDao: TodoDao,todoService: TodoService,networkStatusChecker: NetworkStatusChecker): TodoRepository {
        return TodoRepository(todoDao, todoService,networkStatusChecker)
    }

    @Singleton
    @Provides
    fun provideTodoService(): TodoService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoService::class.java)
    }
}
