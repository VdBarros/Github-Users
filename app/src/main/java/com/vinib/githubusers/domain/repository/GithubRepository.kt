package com.vinib.githubusers.domain.repository

import com.vinib.githubusers.data.util.request.ServiceResult
import com.vinib.githubusers.domain.models.User
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun getUser(login: String): Flow<ServiceResult<User>>
}