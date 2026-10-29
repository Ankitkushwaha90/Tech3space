package com.example.project_try9.data

import com.google.gson.annotations.SerializedName

data class MentorProfile(
    val id: Int,
    val avatar: String? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val content: String? = null,
    @SerializedName("current_role")
    val currentRole: String? = null,
    @SerializedName("current_company")
    val currentCompany: String? = null,
    @SerializedName("years_of_experience")
    val yearsOfExperience: Int? = null,
    val expertise: String? = null,
    val skills: String? = null,
    @SerializedName("github_url")
    val githubUrl: String? = null,
    @SerializedName("linkedin_url")
    val linkedinUrl: String? = null,
    @SerializedName("company_website")
    val companyWebsite: String? = null
)
