package com.example.project_try9.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.project_try9.data.Course

@Composable
fun CourseCard(
    course: Course,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            if (!course.thumbnail.isNullOrBlank()) {
                AsyncImage(
                    model = course.thumbnail
                        .replace("http://", "https://"),
                    contentDescription = course.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = course.shortDescription
                        ?: "Explore this course.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Level: ${course.level ?: "Not specified"}"
                    )
                    Text(
                        "Lessons: ${course.totalLessons ?: 0}"
                    )
                }

                Text(
                    text = "${course.totalTopics ?: course.topics.size} topics • MDX lessons",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
