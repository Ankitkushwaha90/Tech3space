package com.example.project_try9.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FormulaRepository {
    private val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://tech3space.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getFormulas(): List<FormulaCategory> {
        return api.getFormulas()
    }

    suspend fun getFormulaDetailBySlug(slug: String): FormulaItem {
        return api.getFormulaDetailBySlug(slug)
    }
}
