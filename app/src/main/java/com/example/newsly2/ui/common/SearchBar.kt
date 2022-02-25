package com.example.newsly2.ui.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
    isExpanded: Boolean = false,
    onExpand: (Boolean) -> Unit
) {
    //var expanded = isExpanded
    var text by remember { mutableStateOf("") }

    if (isExpanded) {
        TextField(
            value = text,
            singleLine = true,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit(text); text = ""; onExpand(isExpanded) }),
            trailingIcon = { IconButton(onClick = { onExpand(isExpanded) }) { Icon(Icons.Default.Close, "") } },
            modifier = modifier.aspectRatio(3f),
        )
    } else {
        IconButton(onClick = { onExpand(isExpanded) }) {
            Icon(Icons.Outlined.Search, "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarExpandedPreview() {
    SearchBar(onSubmit = {}, isExpanded = true, onExpand = {})
}

@Preview(showBackground = true)
@Composable
fun SearchBarClosedPreview() {
    SearchBar(onSubmit = {}, onExpand = {})
}