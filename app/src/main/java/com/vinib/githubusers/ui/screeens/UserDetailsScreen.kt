package com.vinib.githubusers.ui.screeens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vinib.githubusers.R
import com.vinib.githubusers.data.util.request.ServiceResult
import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.ui.components.ErrorDialog
import com.vinib.githubusers.ui.components.FullScreenLoading
import com.vinib.githubusers.ui.components.RepositoryItem
import com.vinib.githubusers.ui.components.UserItem
import com.vinib.githubusers.ui.theme.GitHubUsersTheme
import com.vinib.githubusers.ui.theme.Purple40
import com.vinib.githubusers.viewModels.UserDetailsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserDetailsScreen(userLogin: String, onBackClicked: () -> Unit) {
    val userDetailsViewModel = hiltViewModel<UserDetailsViewModel>()
    val uiState by userDetailsViewModel.userListingUiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(Unit) {
        userDetailsViewModel.loadUser(userLogin)
    }
    var showErrorMessage by remember { mutableStateOf<String?>(null) }
    if (!showErrorMessage.isNullOrEmpty()) {
        ErrorDialog(text = showErrorMessage.orEmpty()) {
            showErrorMessage = null
        }
    }
    LaunchedEffect(uiState.user.message) {
        if (!uiState.user.message.isNullOrEmpty()) {
            showErrorMessage = uiState.user.message
        }
    }
    if (uiState.user is ServiceResult.Loading) {
        FullScreenLoading()
    } else {
        val user = uiState.user.data
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple40, shape = RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp))
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(
                            id = R.string.description_icon_back
                        ),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onBackClicked() },
                        tint = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        AsyncImage(
                            model = user?.avatarUrl,
                            contentDescription = stringResource(id = R.string.description_user_picture),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(100))
                                .border(2.dp, Purple40, shape = RoundedCornerShape(100))
                                .align(Alignment.Center),
                            placeholder = painterResource(id = R.drawable.ic_logo),
                            error = painterResource(id = R.drawable.ic_logo),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Text(
                    text = userLogin,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Button(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        user?.url?.let {
                            uriHandler.openUri(it)
                        }
                    }, colors = ButtonColors(
                        contentColor = Color.White,
                        containerColor = Color.White,
                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = stringResource(
                            id = R.string.description_icon_link
                        ),
                        modifier = Modifier.size(24.dp),
                        tint = Purple40
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.button_github_link),
                        color = Purple40,
                        fontSize = 16.sp
                    )
                }
            }
            val tabItems = listOf(
                stringResource(id = R.string.tab_repositories),
                stringResource(id = R.string.tab_following),
                stringResource(id = R.string.tab_followers)
            )
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val pagerState = rememberPagerState {
                tabItems.size
            }
            val scope = rememberCoroutineScope()
            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
            }
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, tab ->
                    Tab(selected = index == selectedTabIndex,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = tab)
                        })
                }
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (index) {
                        0 -> {
                            items(user?.repos?.size ?: 0) {
                                user?.repos?.getOrNull(it)?.let { repository ->
                                    RepositoryItem(
                                        repository = repository
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(
                                    color = Purple40,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }

                        1 -> {
                            items(user?.usersFollowing?.size ?: 0) {
                                user?.usersFollowing?.getOrNull(it)?.let { following ->
                                    UserItem(
                                        user = User(
                                            id = following.id,
                                            avatarUrl = following.avatarUrl,
                                            login = following.login
                                        )
                                    ) {}
                                }

                            }
                        }

                        else -> {
                            items(user?.usersFollowers?.size ?: 0) {
                                user?.usersFollowers?.getOrNull(it)?.let { follower ->
                                    UserItem(
                                        user = User(
                                            id = follower.id,
                                            avatarUrl = follower.avatarUrl,
                                            login = follower.login
                                        )
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserDetailsScreen() {
    GitHubUsersTheme {
        UserDetailsScreen(userLogin = "") {

        }
    }
}