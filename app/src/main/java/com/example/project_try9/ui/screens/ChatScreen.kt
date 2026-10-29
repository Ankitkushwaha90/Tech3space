package com.example.project_try9.ui.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen() {
    var message by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Community Chat",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "Connect with developers and mentors."
        )

        Spacer(Modifier.weight(1f))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Write a message") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Send the message to your backend here.
                // Clear this field only after implementing send.
            },
            enabled = message.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}





