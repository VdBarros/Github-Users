package com.vinib.githubusers.data.api

import com.vinib.githubusers.data.util.request.RequestHandler
import com.vinib.githubusers.data.util.request.ServiceResult
import com.vinib.githubusers.domain.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val apiService: ApiService
) : RequestHandler() {

    suspend fun getUser(login: String): Flow<ServiceResult<User>> {
        return makeRequest {
            val user = apiService.getUser(login)
            val followers = apiService.getUserFollowers(login)
            val following = apiService.getUserFollowing(login)
            val repos = apiService.getUserRepos(login)
            user.copy(usersFollowing = following, usersFollowers = followers, repos = repos)
        }
    }

}