package org.captaindmitro.newsly2.ui.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    if (isExpanded) {
        TextField(
            value = text,
            singleLine = true,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit(text); text = ""; onExpand(isExpanded) }),
            trailingIcon = { IconButton(onClick = { onExpand(isExpanded) }) { Icon(Icons.Default.Close, "") } },
            modifier = modifier.aspectRatio(3f).focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.background,
                backgroundColor = MaterialTheme.colors.background.copy(0f),
                trailingIconColor = MaterialTheme.colors.background,
                cursorColor = MaterialTheme.colors.background
            )
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
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