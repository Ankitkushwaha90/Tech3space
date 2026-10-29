package com.example.project_try9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.project_try9.data.MentorProfile
import com.example.project_try9.viewmodel.MentorViewModel

@Composable
fun CommunityScreen(viewModel: MentorViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Mentors, 1: Developers

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Tech3Space Community",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        PrimaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Mentors (${state.mentors.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Developers (${state.developers.size})") }
            )
        }

        Spacer(Modifier.height(16.dp))

        when {
            state.loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && state.mentors.isEmpty() && state.developers.isEmpty() -> {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.error ?: "An error occurred.")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = viewModel::loadCommunity) {
                        Text("Retry")
                    }
                }
            }

            else -> {
                val list = if (selectedTab == 0) state.mentors else state.developers

                if (list.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No profiles available.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(list, key = { it.id }) { profile ->
                            ProfileCard(profile = profile)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(profile: MentorProfile) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val avatarUrl = profile.avatarUrl ?: profile.avatar
                if (!avatarUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = avatarUrl.replace("http://", "https://"),
                        contentDescription = "${profile.firstName} ${profile.lastName}",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${profile.firstName.take(1)}${profile.lastName.take(1)}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${profile.firstName} ${profile.lastName}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (!profile.currentRole.isNullOrBlank()) {
                        Text(
                            text = profile.currentRole,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (!profile.location.isNullOrBlank()) {
                        Text(
                            text = profile.location,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            if (!profile.bio.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!profile.expertise.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Expertise: ${profile.expertise}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (!profile.skills.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Skills: ${profile.skills}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (profile.yearsOfExperience != null && profile.yearsOfExperience > 0) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Experience: ${profile.yearsOfExperience} years",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
