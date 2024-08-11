package net.hiraok.feature.top

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TopScreen() {
    Column {
        Text(text = "test0")
        Text(text = "test1")
    }
}

@Preview
@Composable
fun TopScreenPreview() {
    MaterialTheme {
        TopScreen()
    }
}