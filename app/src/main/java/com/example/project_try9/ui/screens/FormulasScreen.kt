package com.example.project_try9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.project_try9.ui.components.MdxLessonScreen
import com.example.project_try9.viewmodel.FormulaViewModel

@Composable
fun FormulasScreen(viewModel: FormulaViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf("") }

    val selectedFormula = state.selectedFormula

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.detailLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            selectedFormula != null -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    TextButton(
                        onClick = {
                            viewModel.clearSelectedFormula()
                        }
                    ) {
                        Text("← Back to formulas")
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        selectedFormula.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    if (!selectedFormula.shortDescription.isNullOrBlank()) {
                        Text(
                            selectedFormula.shortDescription,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    Text(
                        "Endpoint: api/formulas/formulas/${selectedFormula.slug}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(16.dp))

                    MdxLessonScreen(
                        content = selectedFormula.content ?: "No content available.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Estimated time: ${selectedFormula.estimatedMinutes ?: 30} minutes • ${if (selectedFormula.isFree) "Free" else "Premium"}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            else -> {
                Text(
                    "Engineering Formulas & Math",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Search formulas or categories") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                when {
                    state.loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    state.error != null && state.categories.isEmpty() -> {
                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                            Text(state.error ?: "An error occurred.")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = viewModel::loadFormulas) {
                                Text("Retry")
                            }
                        }
                    }

                    else -> {
                        val filteredCategories = state.categories.map { category ->
                            category.copy(
                                formulas = category.formulas.filter {
                                    it.title.contains(search, true) ||
                                            (it.shortDescription ?: "").contains(search, true) ||
                                            category.title.contains(search, true)
                                }
                            )
                        }.filter {
                            it.title.contains(search, true) || it.formulas.isNotEmpty()
                        }

                        if (filteredCategories.isEmpty()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                Text("No formulas / subtopics found.")
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 20.dp)
                            ) {
                                items(filteredCategories, key = { it.id }) { category ->
                                    ElevatedCard(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(Modifier.padding(16.dp)) {
                                            if (!category.thumbnail.isNullOrBlank()) {
                                                AsyncImage(
                                                    model = category.thumbnail.replace("http://", "https://"),
                                                    contentDescription = category.title,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(140.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Spacer(Modifier.height(12.dp))
                                            }

                                            Text(
                                                category.title,
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                category.shortDescription ?: "",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Spacer(Modifier.height(8.dp))
                                            Text(
                                                "Level: ${category.level ?: "All"} • Est. Hours: ${category.estimatedHours ?: 0}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )

                                            HorizontalDivider(Modifier.padding(vertical = 12.dp))

                                            Text(
                                                "Topics & Formulas (${category.formulas.size})",
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Spacer(Modifier.height(8.dp))

                                            category.formulas.forEach { formula ->
                                                Card(
                                                    onClick = {
                                                        viewModel.selectFormulaBySlug(formula.slug)
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                                ) {
                                                    Column(Modifier.padding(12.dp)) {
                                                        Text(
                                                            formula.title,
                                                            style = MaterialTheme.typography.titleSmall
                                                        )
                                                        if (!formula.shortDescription.isNullOrBlank()) {
                                                            Spacer(Modifier.height(4.dp))
                                                            Text(
                                                                formula.shortDescription,
                                                                style = MaterialTheme.typography.bodySmall
                                                            )
                                                        }
                                                        Spacer(Modifier.height(4.dp))
                                                        Text(
                                                            "Est. ${formula.estimatedMinutes ?: 30} mins • ${if (formula.isFree) "Free" else "Premium"} • api/formulas/formulas/${formula.slug}",
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
            }
        }
    }
}
