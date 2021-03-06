package org.captaindmitro.newsly2.ui.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.compose.rememberImagePainter
import com.example.newsly2.R
import org.captaindmitro.domain.entities.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.captaindmitro.newsly2.navigation.NavDestination
import org.captaindmitro.newsly2.ui.login.AuthViewModel
import org.captaindmitro.newsly2.ui.theme.VeryLightGrey
import org.captaindmitro.newsly2.utils.*

@OptIn(ExperimentalMaterialApi::class)
private fun updateBackdropState(coroutineScope: CoroutineScope, backdropScaffoldState: BackdropScaffoldState, conceal: Boolean = true) {
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
    authViewModel: AuthViewModel,
    navController: NavController,
    userName: String?
) {
    val news = homeViewModel.news.collectAsState()
    val currentQuery = homeViewModel.currentQuery

    val backdropScaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
    val coroutineScope = rememberCoroutineScope()

    val updateCategory: (String) -> Unit = { category ->
        //updateBackdropState(coroutineScope, backdropScaffoldState)
        homeViewModel.changeCategory(category)
    }
    val onClickDetails: (String) -> Unit = { url ->
        val maskUrl = navMaskUrl(url)
        navController.navigate("${NavDestination.DETAILS}/$maskUrl")
    }

    val search: (String) -> Unit = { query ->
        homeViewModel.searchQuery(query)
        updateBackdropState(coroutineScope, backdropScaffoldState)
    }

    val favoriteArticle = homeViewModel::likeArticle
    val navToFavorites: () -> Unit = { navController.navigate(NavDestination.FAVORITE) }
    val isLiked: (Article) -> Boolean = homeViewModel::isArticleLiked
    val signOut = authViewModel::signOut
    val changeLanguage = homeViewModel::changeLanguage

    NewHomeScreenContent(
        news = news.value,
        currentQuery = currentQuery.value,
        backdropScaffoldState = backdropScaffoldState,
        updateCategory = updateCategory,
        onClickDetails = onClickDetails,
        search = search,
        onLikeArticle = favoriteArticle,
        navToFavorites = navToFavorites,
        isLiked = isLiked,
        onLogOut = {
                   signOut {
                       navController.navigate(
                           NavDestination.AUTH,
                           NavOptions.Builder().setPopUpTo("${NavDestination.HOME}/$userName", true).build()
                       )
                   }
        },
        userName = userName,
        onLanguageChange = changeLanguage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewHomeScreenContent(
    news: ApiState,
    currentQuery: String,
    backdropScaffoldState: BackdropScaffoldState,
    updateCategory: (String) -> Unit,
    onClickDetails: (String) -> Unit,
    search: (String) -> Unit,
    onLikeArticle: (Article, Boolean) -> Unit,
    navToFavorites: () -> Unit,
    isLiked: (Article) -> Boolean,
    onLogOut: () -> Unit,
    onLanguageChange: (String) -> Unit,
    userName: String?,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val contentState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(userName, onLogOut, { lang -> onLanguageChange(lang); coroutineScope.launch { scaffoldState.drawerState.close() } }) },
        floatingActionButton = {
            if (contentState.firstVisibleItemIndex > 0)
                Floating(onClick = { coroutineScope.launch { contentState.animateScrollToItem(0) } })
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
    ) {
        when (news) {
            is ApiState.Empty -> Text("Empty")
            is ApiState.Success -> {
                HomeScreenContent(
                    news = news.data,
                    contentState = contentState,
                    currentQuery = currentQuery,
                    backdropScaffoldState = backdropScaffoldState,
                    updateCategory = updateCategory,
                    onClickDetails = onClickDetails,
                    search = search,
                    onLikeArticle = onLikeArticle,
                    navToFavorites = navToFavorites,
                    isLiked = isLiked,
                    openDrawer = { coroutineScope.launch { scaffoldState.drawerState.open() } },
                )
            }
            is ApiState.Error -> { Text(text = news.message.localizedMessage ?: "Unexpected error", modifier = modifier
                .fillMaxSize(1f)
                .wrapContentSize(Alignment.Center)) }
            is ApiState.Loading -> CircularProgressIndicator(modifier = modifier
                .fillMaxSize(1f)
                .wrapContentSize(Alignment.Center))
        }

    }
}

@Composable
fun Floating(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Icon(Icons.Default.KeyboardArrowUp, "")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    news: List<Article>,
    contentState: LazyListState,
    currentQuery: String,
    backdropScaffoldState: BackdropScaffoldState,
    updateCategory: (String) -> Unit,
    onClickDetails: (String) -> Unit,
    search: (String) -> Unit,
    onLikeArticle: (Article, Boolean) -> Unit,
    navToFavorites: () -> Unit,
    isLiked: (Article) -> Boolean,
    openDrawer: () -> Unit,
) {
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = { org.captaindmitro.newsly2.ui.common.TopAppBar(currentQuery, search, navToFavorites, openDrawer) },
        backLayerContent = { CategoriesList(categories = fromCategory.keys.toList(), onClick = updateCategory) },
        backLayerBackgroundColor = MaterialTheme.colors.primary,
        frontLayerContent = { NewsList(state = contentState, news = news, onClick = onClickDetails, onLike = onLikeArticle, isLiked = isLiked) },
        frontLayerBackgroundColor = VeryLightGrey,
        frontLayerScrimColor = Color.Unspecified
    )
}

@Composable
fun Drawer(
    userName: String?,
    onLogOut: () -> Unit,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("English") }

    Surface(
        color = MaterialTheme.colors.primarySurface
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 48.dp)
        ) {
            Text("News")
            Text("Favorites")
            Button(onClick = { expanded = true }) {
                Text(selectedText)
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    fromLanguage.keys.forEach { lang ->
                        DropdownMenuItem(onClick = { selectedText = lang; expanded = false; onLanguageChange(fromLanguage[lang] ?: throw Exception("Unsupported language")) }) {
                            Text(lang)
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp)) {
            Text(text = userName!!)
            Button(onClick = onLogOut) {
                Text("Logout")
            }
        }

    }
}

@Composable
fun CategoryItem(category: String, onClick: (String) -> Unit) {
    Text(
        text = category,
        style = MaterialTheme.typography.subtitle2,
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
//    val transition = rememberInfiniteTransition().animateFloat(
//        initialValue = 1f,
//        targetValue = 0f,
//        animationSpec = infiniteRepeatable(tween(5000, 1000, easing = LinearOutSlowInEasing), repeatMode = RepeatMode.Reverse)
//    )

    LaunchedEffect(isLiked) {
        Log.i("HomeScreen", "${article.title} is $isLiked")
    }

    Card(modifier = modifier
        .wrapContentSize()
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick(article.url) },
    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = article.urlToImage, builder = { crossfade(true); placeholder(R.drawable.image_placeholder) }),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
                //alpha = transition.value
            )
            Column(modifier = modifier
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
                        SocialButtons(article = article, onLike = onLike, likedState = isLiked)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsList(
    state: LazyListState,
    news: List<Article>,
    onClick: (String) -> Unit,
    onLike: (Article, Boolean) -> Unit,
    isLiked: (Article) -> Boolean
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(news, key = { it.id }) {
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
    onLike: (Article, Boolean) -> Unit,
    likedState: Boolean
) {
    val context = LocalContext.current
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
    SocialButtons(fakeArticle, { _, _ ->}, false)
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val category = "Business"
    CategoryItem(category = category, onClick = {})
}
