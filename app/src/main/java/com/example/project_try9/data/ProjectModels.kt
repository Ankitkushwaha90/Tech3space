package com.example.project_try9.data

import com.google.gson.annotations.SerializedName

data class ProjectCreator(
    val id: Int,
    val email: String? = null,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("profile_picture")
    val profilePicture: String? = null,
    @SerializedName("user_type")
    val userType: String? = null
)

data class ProjectItem(
    val id: Int,
    val title: String,
    val slug: String,
    @SerializedName("short_description")
    val shortDescription: String? = null,
    val thumbnail: String? = null,
    val creator: ProjectCreator? = null,
    val technologies: String? = null,
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    @SerializedName("created_at")
    val createdAt: String? = null
)
