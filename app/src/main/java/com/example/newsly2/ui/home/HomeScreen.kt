package com.example.newsly2.ui.home

import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.newsly2.model.Article
import com.example.newsly2.navigation.NavDestination
import com.example.newsly2.utils.fakeArticle
import com.example.newsly2.utils.fromCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
private fun backdropState(coroutineScope: CoroutineScope, backdropScaffoldState: BackdropScaffoldState, conceal: Boolean = true) {
    coroutineScope.launch {
        if (conceal) {
            backdropScaffoldState.conceal()
        } else {
            backdropScaffoldState.reveal()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val news = homeViewModel.news.collectAsState()
    val selectedCategory = homeViewModel.category
    val currentQuery = homeViewModel.currentQuery

    val backdropScaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()

    val updateCategory: (String) -> Unit = { category ->
        backdropState(coroutineScope, backdropScaffoldState)
        homeViewModel.onUpdateCategory(category)
    }
    val onClickDetails: (Article) -> Unit = { article ->
        homeViewModel.onClickDetails(article)
        navController.navigate(NavDestination.DETAILS)
    }

    val search: (String) -> Unit = { query ->
        homeViewModel.onSearch(query)
        backdropState(coroutineScope, backdropScaffoldState)
    }

//    val share: (Context) -> Unit = { context ->
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"))
//        context.startActivity(intent)
//    }

    HomeScreenContent(
        news = news.value,
        currentQuery = currentQuery.value,
        backdropScaffoldState = backdropScaffoldState,
        updateCategory = updateCategory,
        onClickDetails = onClickDetails,
        search = search,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    news: List<Article>,
    currentQuery: String,
    backdropScaffoldState: BackdropScaffoldState,
    updateCategory: (String) -> Unit,
    onClickDetails: (Article) -> Unit,
    search: (String) -> Unit
) {
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = { TopAppBar(
            title = { Text("Newsly2 - ${currentQuery.replaceFirstChar { it.uppercase() }}") },
            actions = { SearchBar(onSubmit = search) },
            backgroundColor = MaterialTheme.colors.primaryVariant
        ) },
        backLayerContent = { CategoriesList(categories = fromCategory.keys.toList(), onClick = updateCategory) },
        backLayerBackgroundColor = MaterialTheme.colors.primary,
        frontLayerContent = { NewsList(news = news, onClick = onClickDetails) },
    )
}

@Composable
fun CategoryItem(category: String, onClick: (String) -> Unit) {
    Text(
        text = category,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick(category.lowercase()) }
    )
}

@Composable
fun CategoriesList(categories: List<String>, onClick: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.horizontalScroll(scrollState)
    ) {
        for (category in categories) {
            CategoryItem(category = category, onClick = onClick)
        }
    }
}

@Composable
fun ArticleItem(article: Article, onClick: (Article) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(modifier = modifier
        .padding(4.dp)
        .wrapContentSize()
        .clip(RoundedCornerShape(12.dp))
        .background(Color.White)
        //.border(BorderStroke(1.dp, MaterialTheme.colors.primaryVariant), RoundedCornerShape(12.dp))
        .clickable { onClick(article) }
        //.padding(8.dp)
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
            Column(modifier = modifier
                .border(
                    BorderStroke(1.dp, MaterialTheme.colors.primaryVariant.copy(alpha = 0.3f)),
                    RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
                .padding(8.dp)
            ) {
                Text(text = article.title, style = MaterialTheme.typography.h6)
                Text(article.description)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.author,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start))
                    Row(modifier= modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                    ) {
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, article.url)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)

                                context.startActivity(shareIntent)
                            }
                        ) { Icon(Icons.Default.Share, "") }
                    }
                }
            }

        }
    }
}

@Composable
fun NewsList(news: List<Article>, onClick: (Article) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(6.dp)) {
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
