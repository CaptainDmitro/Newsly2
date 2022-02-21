package com.example.newsly2.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.newsly2.model.Article

@Composable
fun NewsList(news: List<Article>, navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn {
        items(news) {
            ArticleItem(article = it, onClick = { navController.navigate("details/${it.title}") })
        }
    }
}

@Composable
fun CategoriesList(categories: List<String>, onClick: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier.horizontalScroll(scrollState)
    ) {
        for (category in categories) {
            CategoryItem(category = category, onClick = onClick)
        }
    }
}

@Composable
fun CategoryItem(category: String, onClick: (String) -> Unit) {
    Button(onClick = { onClick(category.lowercase()) }, modifier = Modifier.padding(4.dp)) {
        Text(category)
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val category = "Business"
    CategoryItem(category = category, onClick = {})
}

@Composable
fun ArticleItem(article: Article, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .padding(4.dp)
        .wrapContentSize()
        .border(BorderStroke(2.dp, MaterialTheme.colors.primary), RoundedCornerShape(4.dp))
        .clickable { onClick() }
        .padding(8.dp)
    ) {
        Column {
//            Box(modifier = modifier
//                .height(250.dp)
//                .fillMaxWidth()
//                .background(MaterialTheme.colors.secondary)
//            )
            Image(
                painter = rememberImagePainter(data = article.urlToImage),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
            )
            Spacer(modifier = modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(MaterialTheme.colors.secondaryVariant)
            )
            Text(article.title)
            Text(article.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleItemPreview() {
    val article = Article(
        "Author",
        "Title",
        "description",
        "url",
        "urlToImage",
        "publishedAt",
        "content"
    )

    ArticleItem(article, {})
}
