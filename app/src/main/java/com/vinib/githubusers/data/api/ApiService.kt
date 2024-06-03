package com.vinib.githubusers.data.api

import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.domain.models.UserRepository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("per_page") pageCount: Int = 30
    ): List<User>

    @GET("users/{login}")
    suspend fun getUser(
        @Path("login") login: String
    ): User

    @GET("users/{login}/repos")
    suspend fun getUserRepos(
        @Path("login") login: String
    ): List<UserRepository>

    @GET("users/{login}/followers")
    suspend fun getUserFollowers(
        @Path("login") login: String
    ): List<User>

    @GET("users/{login}/following")
    suspend fun getUserFollowing(
        @Path("login") login: String
    ): List<User>

}