package com.example.newsly2.ui.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit,
    isExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(isExpanded) }
    var text by remember { mutableStateOf("") }

    if (expanded) {
        TextField(
            value = text,
            singleLine = true,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit(text); text = ""; expanded = !expanded }),
            trailingIcon = { IconButton(onClick = { expanded = !expanded }) { Icon(Icons.Outlined.Close, "") } },
            modifier = modifier.aspectRatio(3f),
        )
    } else {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Outlined.Search, "")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SearchBarExpandedPreview() {
    SearchBar(onSubmit = {}, isExpanded = true)
}

@Preview(showBackground = true)
@Composable
fun SearchBarClosedPreview() {
    SearchBar(onSubmit = {})
}