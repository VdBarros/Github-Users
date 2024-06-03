package com.vinib.githubusers.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vinib.githubusers.R
import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.ui.theme.GitHubUsersTheme
import com.vinib.githubusers.ui.theme.Purple40

@Composable
fun UserItem(
    user: User,
    enableClick: Boolean = false,
    onUserClicked: () -> Unit
) {
    val modifier = if (enableClick) {
        Modifier.clickable { onUserClicked() }
    } else {
        Modifier
    }
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, Purple40, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = stringResource(id = R.string.description_user_picture),
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(100))
                .border(2.dp, Purple40, shape = RoundedCornerShape(100))
                .align(Alignment.CenterVertically),
            placeholder = painterResource(id = R.drawable.ic_logo),
            error = painterResource(id = R.drawable.ic_logo),
            contentScale = ContentScale.Fit
        )
        Text(
            text = user.login ?: stringResource(
                id = R.string.placeholder_username,
                user.id.toString()
            ),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun PreviewUserItem() {
    GitHubUsersTheme {
        UserItem(User(id = 1, avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4")) {}
    }
}