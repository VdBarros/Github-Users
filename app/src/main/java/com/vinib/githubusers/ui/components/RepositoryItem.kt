package com.vinib.githubusers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vinib.githubusers.domain.models.UserRepository
import com.vinib.githubusers.ui.theme.GitHubUsersTheme

@Composable
fun RepositoryItem(repository: UserRepository) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = repository.name.orEmpty(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = repository.description.orEmpty(), fontSize = 12.sp)
    }
}

@Composable
@Preview
fun PreviewRepositoryItem() {
    GitHubUsersTheme {
        RepositoryItem(
            repository = UserRepository(
                1,
                name = "Repositório",
                description = "Descrição"
            )
        )
    }
}