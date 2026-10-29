package com.example.project_try9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_try9.viewmodel.CourseUiState

@Composable
fun DashboardScreen(
    state: CourseUiState,
    onCoursesClick: () -> Unit,
    onFormulasClick: () -> Unit,
    onProjectsClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Tech3Space Hub",
            style = MaterialTheme.typography.headlineLarge
        )

        Text("Learn. Build. Connect. Master Engineering & AI.")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onCoursesClick, modifier = Modifier.weight(1f)) {
                Text("Courses")
            }
            Button(onClick = onFormulasClick, modifier = Modifier.weight(1f)) {
                Text("Formulas")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onProjectsClick, modifier = Modifier.weight(1f)) {
                Text("Projects")
            }
            Button(onClick = onChatClick, modifier = Modifier.weight(1f)) {
                Text("Chat")
            }
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "Featured Learning Library",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${state.courses.size} active courses available on tech3space.com"
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = onCoursesClick) {
                    Text("Explore All Courses")
                }
            }
        }

        Text(
            "Featured Courses",
            style = MaterialTheme.typography.titleLarge
        )

        val featured = state.courses.filter { it.isFeatured }
        if (featured.isNotEmpty()) {
            featured.take(3).forEach { course ->
                Text("• ${course.title} (${course.level ?: "General"})")
            }
        } else if (state.courses.isNotEmpty()) {
            state.courses.take(3).forEach { course ->
                Text("• ${course.title} (${course.level ?: "General"})")
            }
        } else {
            Text("Loading courses from API...")
        }
    }
}
