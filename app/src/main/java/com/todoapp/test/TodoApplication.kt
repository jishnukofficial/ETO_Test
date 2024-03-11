package com.todoapp.test



import android.app.Application
import com.todoapp.test.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application()
