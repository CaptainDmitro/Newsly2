package com.example.newsly2.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.newsly2.model.Article
import com.example.newsly2.navigation.NavDestination
import com.example.newsly2.utils.fakeArticle
import com.example.newsly2.utils.fromCategory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val news = homeViewModel.news.collectAsState()
    val selectedCategory = homeViewModel.category

    val backdropScaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()

    val updateCategory: (String) -> Unit = { category ->
        coroutineScope.launch {
            backdropScaffoldState.conceal()
        }
        homeViewModel.onUpdateCategory(category)
    }
    val onClickDetails: (Article) -> Unit = { article ->
        homeViewModel.onClickDetails(article)
        navController.navigate(NavDestination.DETAILS)
    }


    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = { TopAppBar(
            title = { Text("Newsly2 - ${selectedCategory.value.replaceFirstChar { it.uppercase() }}") },
        ) },
        backLayerContent = { CategoriesList(categories = fromCategory.keys.toList(), onClick = updateCategory) },
        frontLayerContent = { NewsList(news = news.value, onClick = onClickDetails) },
    )
}

@Composable
fun CategoryItem(category: String, onClick: (String) -> Unit) {
    Button(onClick = { onClick(category.lowercase()) }, modifier = Modifier.padding(4.dp)) {
        Text(category)
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
fun ArticleItem(article: Article, onClick: (Article) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .padding(4.dp)
        .wrapContentSize()
        .border(BorderStroke(2.dp, MaterialTheme.colors.primary), RoundedCornerShape(4.dp))
        .clickable { onClick(article) }
        .padding(8.dp)
    ) {
        Column {
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

@Composable
fun NewsList(news: List<Article>, onClick: (Article) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn {
        items(news) {
            ArticleItem(
                article = it,
                onClick = onClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleItemPreview() {
    ArticleItem(fakeArticle, {})
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val category = "Business"
    CategoryItem(category = category, onClick = {})
}
