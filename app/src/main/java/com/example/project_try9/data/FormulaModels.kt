package com.example.project_try9.data

import com.google.gson.annotations.SerializedName

data class FormulaCategory(
    val id: Int,
    val instructor: Int? = null,
    val title: String,
    val slug: String,
    @SerializedName("short_description")
    val shortDescription: String? = null,
    val thumbnail: String? = null,
    val level: String? = null,
    @SerializedName("estimated_hours")
    val estimatedHours: Int? = null,
    @SerializedName("total_formulas")
    val totalFormulas: Int? = null,
    @SerializedName("total_lessons")
    val totalLessons: Int? = null,
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    val formulas: List<FormulaItem> = emptyList()
)

data class FormulaItem(
    val id: Int,
    val title: String,
    val slug: String,
    @SerializedName("short_description")
    val shortDescription: String? = null,
    val content: String? = null,
    @SerializedName("estimated_minutes")
    val estimatedMinutes: Int? = null,
    @SerializedName("is_free")
    val isFree: Boolean = false,
    @SerializedName("order")
    val order: Int? = null
)
