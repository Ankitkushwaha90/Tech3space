package com.example.project_try9.data

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CourseRepository {

    private val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://tech3space.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getCourses(): List<Course> {
        return try {
            Log.d("CourseAPI", "Requesting courses...")

            val courses = api.getCourses()

            Log.d("CourseAPI", "Total courses fetched: ${courses.size}")

            courses.forEach { course ->
                Log.d("CourseAPI", "Course ID: ${course.id}")
                Log.d("CourseAPI", "Title: ${course.title}")
                Log.d("CourseAPI", "Slug: ${course.slug}")
                Log.d("CourseAPI", "Description: ${course.shortDescription}")
                Log.d("CourseAPI", "Thumbnail: ${course.thumbnail}")
                Log.d("CourseAPI", "Level: ${course.level}")
                Log.d("CourseAPI", "Topics: ${course.topics.size}")

                course.topics.forEach { topic ->
                    Log.d(
                        "CourseAPI",
                        "Topic: ${topic.id} - ${topic.title}, Free: ${topic.isFree}"
                    )
                }
            }

            courses
        } catch (exception: Exception) {
            Log.e("CourseAPI", "Failed to fetch courses", exception)
            throw exception
        }
    }

    suspend fun getCourseBySlug(slug: String): Course {
        return api.getCourseBySlug(slug)
    }

    suspend fun getTopicBySlug(courseSlug: String, topicSlug: String): CourseTopic {
        return api.getTopicBySlug(courseSlug, topicSlug)
    }

    suspend fun getTopicDetailBySlug(slug: String): CourseTopic {
        return api.getTopicDetailBySlug(slug)
    }
}
