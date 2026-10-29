package com.example.project_try9.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectRepository {
    private val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://tech3space.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getProjects(): List<ProjectItem> {
        return api.getProjects()
    }
}
