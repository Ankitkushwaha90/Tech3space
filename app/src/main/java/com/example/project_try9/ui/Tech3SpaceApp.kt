package com.example.project_try9.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_try9.ui.components.MdxLessonScreen
import com.example.project_try9.ui.screens.*
import com.example.project_try9.viewmodel.CourseViewModel

private val tabs = listOf(
    "Dashboard",
    "Courses",
    "Formulas",
    "Projects",
    "Chat"
)

@Composable
fun Tech3SpaceApp() {
    val vm: CourseViewModel = viewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }

    val selectedCourse = state.selectedCourse
    val selectedTopic = state.selectedTopic

    MaterialTheme {
        Scaffold(
            bottomBar = {
                if (selectedCourse == null && selectedTopic == null) {
                    NavigationBar {
                        tabs.forEachIndexed { index, label ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = {
                                    selectedTab = index
                                },
                                icon = {
                                    Text(
                                        when (index) {
                                            0 -> "⌂"
                                            1 -> "▤"
                                            2 -> "∑"
                                            3 -> "📂"
                                            else -> "✉"
                                        }
                                    )
                                },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }
        ) { padding ->

            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when {
                    state.detailLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    selectedTopic != null -> {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    vm.clearSelectedTopic()
                                }
                            ) {
                                Text("← Back to course topics")
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                selectedTopic.title,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            if (!selectedTopic.shortDescription.isNullOrBlank()) {
                                Text(
                                    selectedTopic.shortDescription,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(Modifier.height(12.dp))
                            }

                            MdxLessonScreen(
                                content = selectedTopic.content ?: selectedTopic.description ?: "No content available.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "Estimated time: ${selectedTopic.estimatedMinutes ?: 30} minutes • ${if (selectedTopic.isFree) "Free" else "Premium"}",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    selectedCourse != null -> {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    vm.clearSelection()
                                }
                            ) {
                                Text("← Back to courses list")
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                selectedCourse.title,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                selectedCourse.shortDescription ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "Level: ${selectedCourse.level ?: "Not specified"} • Est. Hours: ${selectedCourse.estimatedHours ?: 0}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                "Course Curriculum & Sub-slug Topics",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(Modifier.height(8.dp))

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(bottom = 20.dp)
                            ) {
                                items(
                                    selectedCourse.topics.sortedBy {
                                        it.order ?: Int.MAX_VALUE
                                    },
                                    key = { it.id }
                                ) { topic ->
                                    ElevatedCard(
                                        onClick = {
                                            vm.selectTopicBySlug(selectedCourse.slug, topic.slug)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        ListItem(
                                            headlineContent = {
                                                Text(topic.title)
                                            },
                                            supportingContent = {
                                                Text(
                                                    topic.shortDescription ?: "Endpoint: api/courses/topics/${topic.slug}"
                                                )
                                            },
                                            trailingContent = {
                                                Text(
                                                    if (topic.isFree) "Free"
                                                    else "Locked"
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        when (selectedTab) {
                            0 -> DashboardScreen(
                                state = state,
                                onCoursesClick = { selectedTab = 1 },
                                onFormulasClick = { selectedTab = 2 },
                                onProjectsClick = { selectedTab = 3 },
                                onChatClick = { selectedTab = 4 }
                            )

                            1 -> CoursesScreen(
                                viewModel = vm,
                                onCourseClick = { slug ->
                                    vm.selectCourseBySlug(slug)
                                }
                            )

                            2 -> FormulasScreen()
                            3 -> ProjectsScreen()
                            4 -> ChatScreen()
                        }
                    }
                }
            }
        }
    }
}
