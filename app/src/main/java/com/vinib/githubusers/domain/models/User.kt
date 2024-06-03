package com.vinib.githubusers.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = false) val id: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("html_url")
    val url: String? = null,
    val login: String? = null,
    val usersFollowing: List<User>? = emptyList(),
    val usersFollowers: List<User>? = emptyList(),
    val repos: List<UserRepository>? = emptyList()
) : Serializable
