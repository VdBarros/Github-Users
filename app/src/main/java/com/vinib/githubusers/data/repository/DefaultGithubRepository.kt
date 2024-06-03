package com.vinib.githubusers.data.repository

import com.vinib.githubusers.data.api.ApiClient
import com.vinib.githubusers.data.util.request.ServiceResult
import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultGithubRepository @Inject constructor(
    private val apiClient: ApiClient
) : GithubRepository {

    override suspend fun getUser(login: String): Flow<ServiceResult<User>> {
        return apiClient.getUser(login)
    }

}
