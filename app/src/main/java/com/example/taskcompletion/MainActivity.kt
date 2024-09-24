package com.example.taskcompletion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import com.example.taskcompletion.ui.theme.TaskCompletionTheme

// Task data class to hold task information
data class Task(
    val description: String,
    var isCompleted: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskCompletionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        TaskManagerApp()
                    }
                )
            }
        }
    }
}

// Composable function for the task manager app
@Composable
fun TaskManagerApp() {
    var taskDescription by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Input for task description
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Enter a task") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (taskDescription.isNotEmpty()) {
                        tasks = tasks + Task(taskDescription)
                        taskDescription = ""
                    }
                })
            )
            Button(
                onClick = {
                    if (taskDescription.isNotEmpty()) {
                        tasks = tasks + Task(taskDescription)
                        taskDescription = ""
                    }
                },
                modifier = Modifier.alignByBaseline()
            ) {
                Text("Add Task")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task list display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            tasks.forEachIndexed { index, task ->
                TaskRow(
                    task = task,
                    onTaskCheckedChange = { isChecked ->
                        tasks = tasks.mapIndexed { i, t ->
                            if (i == index) t.copy(isCompleted = isChecked) else t
                        }
                    }
                )
            }
        }

        // Clear completed tasks button
        Button(
            onClick = {
                tasks = tasks.filter { !it.isCompleted }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Completed Tasks")
        }
    }
}

// Composable function to display each task in a row
@Composable
fun TaskRow(task: Task, onTaskCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onTaskCheckedChange
        )
        Text(
            text = task.description,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    TaskCompletionTheme {
        TaskManagerApp()
    }
}