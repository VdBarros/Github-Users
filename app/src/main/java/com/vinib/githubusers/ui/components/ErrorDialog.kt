package com.vinib.githubusers.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vinib.githubusers.R
import com.vinib.githubusers.ui.theme.GitHubUsersTheme
import com.vinib.githubusers.ui.theme.Purple40

@Composable
fun ErrorDialog(text: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier.padding(vertical = 14.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.label_atention),
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = text,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(id = R.string.label_close),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewErrorDialog() {
    GitHubUsersTheme {
        ErrorDialog(
            "Erro Teste",
            onDismiss = {}
        )
    }
}