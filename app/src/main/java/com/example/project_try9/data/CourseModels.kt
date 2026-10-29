package com.example.project_try9.data

import com.google.gson.annotations.SerializedName

data class Course(
    val id: Int,
    val instructor: Any? = null,
    val title: String,
    val slug: String,
    @SerializedName("short_description")
    val shortDescription: String? = null,
    val description: String? = null,
    val content: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    val level: String? = null,
    @SerializedName("estimated_hours")
    val estimatedHours: Int? = null,
    @SerializedName("total_topics")
    val totalTopics: Int? = null,
    @SerializedName("total_lessons")
    val totalLessons: Int? = null,
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    val topics: List<CourseTopic> = emptyList()
)

data class CourseTopic(
    val id: Int,
    val title: String,
    val slug: String,
    @SerializedName("short_description")
    val shortDescription: String? = null,
    val description: String? = null,
    val content: String? = null,
    @SerializedName("estimated_minutes")
    val estimatedMinutes: Int? = null,
    @SerializedName("is_free")
    val isFree: Boolean = false,
    @SerializedName("order")
    val order: Int? = null
)
