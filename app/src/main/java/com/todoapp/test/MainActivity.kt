package com.todoapp.test



import androidx.activity.compose.setContent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewmodel.compose.viewModel
import com.todoapp.test.data.remote.NetworkChangeReceiver
import com.todoapp.test.ui.screen.TodoScreen

import com.todoapp.test.ui.theme.TestTheme
import com.todoapp.test.ui.viewModel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var todoViewModel: TodoViewModel // Remove injection

    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private lateinit var networkIntentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModel using ViewModelProvider
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        // Create BroadcastReceiver and IntentFilter
        networkChangeReceiver = NetworkChangeReceiver { isConnected ->
            if (isConnected) {
                // Internet connection is available, trigger list update
                todoViewModel.updateTodoList()
            }
        }
        networkIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        // Register BroadcastReceiver
        registerReceiver(networkChangeReceiver, networkIntentFilter)

        setContent {
            TestTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TodoScreen(todoViewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister BroadcastReceiver
        unregisterReceiver(networkChangeReceiver)
    }
}


