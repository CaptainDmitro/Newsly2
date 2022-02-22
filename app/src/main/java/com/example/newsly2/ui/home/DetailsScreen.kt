package com.example.newsly2.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.newsly2.model.Article
import com.example.newsly2.utils.fakeArticle

@Composable
fun DetailsScreen(
    homeViewModel: HomeViewModel
) {
    val article = homeViewModel.detailsItem

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Newsly2") },
                navigationIcon = { IconButton(onClick = { /*TODO: implement back button*/ }) {
                    Icon(Icons.Filled.ArrowBack, "")
                } },
                backgroundColor = MaterialTheme.colors.primaryVariant
            )
        }
    ) {
        Details(article.value)
    }
}

@Composable
fun Details(article: Article, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(6.dp)) {
        // TODO: Reuse already downloaded image from news feed
        Image(
            painter = rememberImagePainter(data = article.urlToImage),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(2f)
        )
        Spacer(modifier = modifier.height(8.dp))
        Row {
            Text(article.author, modifier = modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start))
            Text(article.publishedAt, modifier = modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End))
        }
        Spacer(modifier = modifier.height(4.dp))
        Text(article.title, style = MaterialTheme.typography.h6)
        Spacer(modifier = modifier.height(4.dp))
        Text(article.content)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    Details(fakeArticle)
}