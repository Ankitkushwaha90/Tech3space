package com.example.project_try9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.project_try9.viewmodel.ProjectViewModel

@Composable
fun ProjectsScreen(viewModel: ProjectViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Tech3Space Projects & Research",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Search projects or technologies") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        when {
            state.loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && state.projects.isEmpty() -> {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.error ?: "An error occurred.")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = viewModel::loadProjects) {
                        Text("Retry")
                    }
                }
            }

            else -> {
                val filtered = state.projects.filter {
                    it.title.contains(search, true) ||
                            (it.shortDescription ?: "").contains(search, true) ||
                            (it.technologies ?: "").contains(search, true)
                }

                if (filtered.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No projects found.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(filtered, key = { it.id }) { project ->
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    if (!project.thumbnail.isNullOrBlank()) {
                                        AsyncImage(
                                            model = project.thumbnail.replace("http://", "https://"),
                                            contentDescription = project.title,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(160.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(Modifier.height(12.dp))
                                    }

                                    Text(
                                        project.title,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        project.shortDescription ?: "",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    if (!project.technologies.isNullOrBlank()) {
                                        Text(
                                            "Technologies: ${project.technologies}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    if (project.creator != null) {
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            "Creator: ${project.creator.fullName ?: "${project.creator.firstName} ${project.creator.lastName}"} (${project.creator.userType})",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
