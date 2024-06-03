package com.vinib.githubusers.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinib.githubusers.data.util.request.ServiceResult
import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.domain.usecase.GetGithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserDetailsUiState(
    val user: ServiceResult<User> = ServiceResult.Empty()
)

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getGithubUserUseCase: GetGithubUserUseCase
) : ViewModel() {

    private val _userDetailsUiState = MutableStateFlow(UserDetailsUiState())

    val userListingUiState = _userDetailsUiState.asStateFlow()


    fun loadUser(login: String) = viewModelScope.launch {
        getGithubUserUseCase.invoke(login).collect { result ->
            _userDetailsUiState.update {
                it.copy(user = result)
            }
        }
    }
}