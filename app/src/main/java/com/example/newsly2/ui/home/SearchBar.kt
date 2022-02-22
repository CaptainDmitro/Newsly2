package com.example.newsly2.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    onSubmit: (String) -> Unit,
    isExpanded: Boolean = false,
    modifier: Modifier = Modifier
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
    SearchBar({}, true)
}

@Preview(showBackground = true)
@Composable
fun SearchBarClosedPreview() {
    SearchBar({})
}