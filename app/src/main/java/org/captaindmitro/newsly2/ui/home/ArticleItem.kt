package org.captaindmitro.newsly2.ui.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.newsly2.R
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.newsly2.utils.fakeArticle

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

    Card(modifier = modifier
        .wrapContentSize()
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick(article.url) },
    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = article.urlToImage, builder = { crossfade(true); placeholder(
                    R.drawable.image_placeholder) }),
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
    ArticleItem(fakeArticle, {}, { _, _ ->}, false)
}

@Preview(showBackground = true)
@Composable
fun SocialButtonsPreview() {
    SocialButtons(fakeArticle, { _, _ ->}, false)
}