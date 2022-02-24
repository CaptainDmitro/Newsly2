package com.example.newsly2.ui.home

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.newsly2.R
import com.example.newsly2.model.Article
import com.example.newsly2.navigation.NavDestination
import com.example.newsly2.ui.common.SearchBar
import com.example.newsly2.utils.fakeArticle
import com.example.newsly2.utils.fromCategory
import com.example.newsly2.utils.navMaskUrl
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
        homeViewModel.onChangeCategory(category)
    }
    val onClickDetails: (String) -> Unit = { url ->
        val maskUrl = navMaskUrl(url)
        navController.navigate("${NavDestination.DETAILS}/$maskUrl")
    }

    val search: (String) -> Unit = { query ->
        homeViewModel.onSearch(query)
        backdropState(coroutineScope, backdropScaffoldState)
    }

    val favoriteArticle = homeViewModel::onLikeArticle
    val navToFavorites: () -> Unit = { navController.navigate(NavDestination.FAVORITE) }
    val isLiked: (Article) -> Boolean = homeViewModel::isArticleLiked

    HomeScreenContent(
        news = news.value,
        currentQuery = currentQuery.value,
        backdropScaffoldState = backdropScaffoldState,
        updateCategory = updateCategory,
        onClickDetails = onClickDetails,
        search = search,
        onLikeArticle = favoriteArticle,
        navToFavorites = navToFavorites,
        isLiked = isLiked
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    news: List<Article>,
    currentQuery: String,
    backdropScaffoldState: BackdropScaffoldState,
    updateCategory: (String) -> Unit,
    onClickDetails: (String) -> Unit,
    search: (String) -> Unit,
    onLikeArticle: (Article, Boolean) -> Unit,
    navToFavorites: () -> Unit,
    isLiked: (Article) -> Boolean
) {
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = { TopAppBar(
            title = { Text("${stringResource(id = R.string.app_name)} - ${currentQuery.replaceFirstChar { it.uppercase() }}") },
            actions = {
                IconButton(onClick = navToFavorites) {
                    Icon(Icons.Default.Favorite, "")
                }
                SearchBar(onSubmit = search)
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        ) },
        backLayerContent = { CategoriesList(categories = fromCategory.keys.toList(), onClick = updateCategory) },
        backLayerBackgroundColor = MaterialTheme.colors.primary,
        frontLayerContent = { NewsList(news = news, onClick = onClickDetails, onLike = onLikeArticle, isLiked = isLiked) },
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
fun ArticleItem(
    article: Article,
    onClick: (String) -> Unit,
    onLike: (Article, Boolean) -> Unit,
    isLiked: Boolean,
    modifier: Modifier = Modifier
) {
    // TODO: Should it be hoisted? Is it the correct way to obtain context? Think it over.
    val context = LocalContext.current

    Card(modifier = modifier
        .wrapContentSize()
        .clip(RoundedCornerShape(12.dp))
        //.background(Color.White)
        .clickable { onClick(article.url) }
    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = article.urlToImage, builder = { placeholder(R.drawable.image_placeholder) }),
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
                        SocialButtons(article = article, context = context, onLike = onLike, likedState = isLiked)
                    }
                }
            }
        }

    }
}

@Composable
fun NewsList(
    news: List<Article>,
    onClick: (String) -> Unit,
    onLike: (Article, Boolean) -> Unit,
    isLiked: (Article) -> Boolean
) {
    val columnState = rememberLazyListState()
    LazyColumn(
        state = columnState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(news) {
            ArticleItem(
                article = it,
                onClick = onClick,
                onLike = onLike,
                isLiked = isLiked(it)
            )
        }
    }
}

@Composable
fun SocialButtons(
    article: Article,
    context: Context,
    onLike: (Article, Boolean) -> Unit,
    likedState: Boolean
) {
    var isLiked by remember { mutableStateOf(likedState) }

    val likeButtonIcon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp

    IconButton(
        onClick = {
            onLike(article, !isLiked)
            isLiked = !isLiked
        }
    ) {
        Icon(likeButtonIcon, "")
    }
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

@Preview(showBackground = true)
@Composable
fun ArticleItemPreview() {
    ArticleItem(fakeArticle, {}, {_, _ ->}, false)
}

@Preview(showBackground = true)
@Composable
fun SocialButtonsPreview() {
    SocialButtons(fakeArticle, LocalContext.current, {_, _ ->}, false)
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val category = "Business"
    CategoryItem(category = category, onClick = {})
}
