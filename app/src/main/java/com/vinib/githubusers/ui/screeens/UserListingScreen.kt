package com.vinib.githubusers.ui.screeens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.vinib.githubusers.R
import com.vinib.githubusers.ui.components.ErrorDialog
import com.vinib.githubusers.ui.components.FullScreenLoading
import com.vinib.githubusers.ui.components.UserItem
import com.vinib.githubusers.ui.components.UserNotFound
import com.vinib.githubusers.ui.theme.GitHubUsersTheme
import com.vinib.githubusers.ui.theme.Purple40
import com.vinib.githubusers.utils.Timer
import com.vinib.githubusers.viewModels.UserListingViewModel

@Composable
fun UserListingScreen(onUserClicked: (String) -> Unit) {
    val userListingViewModel = hiltViewModel<UserListingViewModel>()
    val users = userListingViewModel.usersPagingFlow.collectAsLazyPagingItems()
    var showErrorMessage by remember { mutableStateOf<String?>(null) }
    if (users.loadState.refresh is LoadState.Loading) {
        FullScreenLoading()
    }
    if (!showErrorMessage.isNullOrEmpty()) {
        ErrorDialog(text = showErrorMessage.orEmpty()) {
            showErrorMessage = null
        }
    }
    LaunchedEffect(users.loadState) {
        if (users.loadState.refresh is LoadState.Error) {
            showErrorMessage = (users.loadState.refresh as LoadState.Error).error.message
        }
    }
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple40, shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp))
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(24.dp))
                val searchText by userListingViewModel.searchText.collectAsState()
                var timer by remember { mutableStateOf(Timer {}) }
                TextField(
                    value = searchText,
                    onValueChange = {
                        userListingViewModel.onSearchTextChanged(it)
                        if (it.isNotEmpty()) {
                            timer.stop()
                            timer = Timer {
                                onUserClicked(it)
                            }
                            timer.start(1000)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(text = stringResource(id = R.string.field_search_placeholder))
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(
                                id = R.string.description_icon_search
                            ),
                            tint = Purple40,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        timer.stop()
                        onUserClicked(searchText)
                    })
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (users.loadState.refresh !is LoadState.Loading) {
            if (users.itemCount < 1) {
                item {
                    UserNotFound()
                }
            } else {
                items(users.itemCount) {
                    users[it]?.let { user ->
                        UserItem(
                            user = user,
                            enableClick = true
                        ) {
                            onUserClicked(user.login.toString())
                        }
                    }
                }
                item {
                    if (users.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewUserListingScreen() {
    GitHubUsersTheme {
        UserListingScreen {

        }
    }
}