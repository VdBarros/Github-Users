package com.vinib.githubusers.domain.models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserRepository(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String? = null,
    val login: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    val description: String? = null,
    val homepage: String? = null,
    val language: String? = null,
    val private: Boolean = false,
    val watchers: Int = 0,
    val forks: Int = 0
) : Serializable
