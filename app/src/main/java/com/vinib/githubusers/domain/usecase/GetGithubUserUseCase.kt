package com.vinib.githubusers.domain.usecase

import com.vinib.githubusers.data.util.request.ServiceResult
import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGithubUserUseCase @Inject constructor(private val repository: GithubRepository) {
    suspend operator fun invoke(login: String): Flow<ServiceResult<User>> {
        return repository.getUser(login)
    }
}