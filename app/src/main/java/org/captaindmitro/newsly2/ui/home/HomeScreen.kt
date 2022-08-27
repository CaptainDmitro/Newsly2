package org.captaindmitro.newsly2.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.newsly2.navigation.NavDestination
import org.captaindmitro.newsly2.ui.Drawer
import org.captaindmitro.newsly2.ui.Fab
import org.captaindmitro.newsly2.ui.common.NewslyTopBar
import org.captaindmitro.newsly2.ui.login.AuthViewModel
import org.captaindmitro.newsly2.ui.theme.VeryLightGrey
import org.captaindmitro.newsly2.utils.ApiState
import org.captaindmitro.newsly2.utils.fromCategory

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
    onUpdateCategory: (String) -> Unit,
    onClickDetails: (String) -> Unit,
    onChangeLanguage: (String) -> Unit,
    onFavoriteArticle: (Article, Boolean) -> Unit,
    onNavToFavorites: () -> Unit,
    isLiked: (Article) -> Boolean,
    userName: String?
) {
    val news by homeViewModel.news.collectAsState()
    val currentQuery by homeViewModel.currentQuery.collectAsState()
    val contentListState = rememberLazyListState()

    val scaffoldState = rememberScaffoldState()
    val backdropScaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
    val coroutineScope = rememberCoroutineScope()

    val onSearch: (String) -> Unit = { query ->
        homeViewModel.searchQuery(query)
        updateBackdropState(coroutineScope, backdropScaffoldState)
    }
    val openDrawer: () -> Unit = {
        coroutineScope.launch { scaffoldState.drawerState.open() }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            if (contentListState.firstVisibleItemIndex > 1) {
                Fab {
                    coroutineScope.launch { contentListState.animateScrollToItem(0) }
                }
            }
        },
        drawerContent = {
//            Drawer(
//                userName = userName,
//                onLogOut = { authViewModel.signOut { navController.navigate(NavDestination.AUTH) } },
//                onLanguageChange = onChangeLanguage
//            )
        }
    ) { paddingValues ->
        HomeScreenContent(
            news = news,
            contentState = contentListState,
            currentQuery = currentQuery,
            backdropScaffoldState = backdropScaffoldState,
            updateCategory = onUpdateCategory,
            onClickDetails = onClickDetails,
            search = onSearch,
            onLikeArticle = onFavoriteArticle,
            navToFavorites = onNavToFavorites,
            isLiked = isLiked,
            openDrawer = openDrawer,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    news: ApiState,
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
    modifier: Modifier = Modifier
) {
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = { NewslyTopBar(currentQuery, search, navToFavorites, openDrawer) },
        backLayerContent = { CategoriesList(categories = fromCategory.keys.toList(), onClick = updateCategory) },
        backLayerBackgroundColor = MaterialTheme.colors.primary,
        frontLayerContent = {
            when (val state = news) {
                is ApiState.Empty -> Text("Empty")
                is ApiState.Success -> {
                    NewsList(
                        state = contentState,
                        news = state.data,
                        onClick = onClickDetails,
                        onLike = onLikeArticle,
                        isLiked = isLiked)
                }
                is ApiState.Error -> { Text(text = state.message.localizedMessage ?: "Unexpected error", modifier = Modifier
                    .fillMaxSize(1f)
                    .wrapContentSize(Alignment.Center)) }
                is ApiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .wrapContentSize(Alignment.Center)
                )
            }
        },
        frontLayerBackgroundColor = VeryLightGrey,
        frontLayerScrimColor = Color.Unspecified,
        modifier = Modifier.then(modifier)
    )
}
