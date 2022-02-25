package com.example.newsly2.ui.favorites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.newsly2.R
import com.example.newsly2.ui.home.HomeViewModel
import com.example.newsly2.ui.home.NewsList

// TODO: Bug: when article is removed from favorite list, the previous one (if >= 2 article) is set to unliked (occurs to display correctly once screen is refreshed)

@Composable
fun FavoriteScreen(homeViewModel: HomeViewModel) {
    val favArticles = homeViewModel.favoriteArticles.collectAsState()
    val onLikeArticle = homeViewModel::onLikeArticle
    val isLiked = homeViewModel::isArticleLiked

    if (favArticles.value.isEmpty()) {
        Text(
            text = stringResource(id = R.string.no_favorites),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center),
            style = MaterialTheme.typography.h6
        )
    } else {
        NewsList(news = favArticles.value, onClick = {}, onLike = onLikeArticle, isLiked = isLiked, state = rememberLazyListState())
    }
}