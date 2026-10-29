package com.example.project_try9.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/courses/")
    suspend fun getCourses(): List<Course>

    @GET("api/courses/{slug}/")
    suspend fun getCourseBySlug(@Path("slug") slug: String): Course

    @GET("api/courses/{courseSlug}/{topicSlug}/")
    suspend fun getTopicBySlug(
        @Path("courseSlug") courseSlug: String,
        @Path("topicSlug") topicSlug: String
    ): CourseTopic

    @GET("api/courses/topics/{slug}/")
    suspend fun getTopicDetailBySlug(@Path("slug") slug: String): CourseTopic

    @GET("api/formulas/")
    suspend fun getFormulas(): List<FormulaCategory>

    @GET("api/formulas/formulas/{slug}/")
    suspend fun getFormulaDetailBySlug(@Path("slug") slug: String): FormulaItem

    @GET("api/mentors/")
    suspend fun getMentors(): List<MentorProfile>

    @GET("api/developers/")
    suspend fun getDevelopers(): List<MentorProfile>

    @GET("api/projects/")
    suspend fun getProjects(): List<ProjectItem>
}
