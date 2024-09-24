package com.example.taskcompletion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.taskcompletion.ui.theme.TaskCompletionTheme

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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEDE7F6))  // Light purple background for the whole screen
                ) {
                    TaskManagerApp()
                }
            }
        }
    }
}

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)) // Purple button
            ) {
                Text("Add Task", color = Color.White)
            }
        }

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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)) // Red button for "Clear Completed"
        ) {
            Text("Clear Completed Tasks", color = Color.White)
        }
    }
}

// Composable function to display each task in a row with a custom color scheme
@Composable
fun TaskRow(task: Task, onTaskCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(if (task.isCompleted) Color(0xFF81C784) else Color(0xFFFFF176)) // Green if completed, Yellow if pending
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onTaskCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF6200EA)) // Purple checkbox
        )
        Text(
            text = task.description,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                color = if (task.isCompleted) Color(0xFF388E3C) else Color.Black  // Dark green for completed, black for pending
            )
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