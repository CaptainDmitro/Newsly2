package com.example.newsly2.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsly2.database.toDaoModel
import com.example.newsly2.model.Article
import com.example.newsly2.repository.Repository
import com.example.newsly2.utils.ApiState
import com.example.newsly2.utils.DEFAULT_CATEGORY
import com.example.newsly2.utils.DEFAULT_COUNTRY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _category = mutableStateOf(DEFAULT_CATEGORY)
    val category: State<String> = _category

    private val _language = mutableStateOf(DEFAULT_COUNTRY)
    val language: State<String> = _language

    private val _currentQuery = mutableStateOf(category.value)
    val currentQuery: State<String> = _currentQuery

    private val _news = MutableStateFlow<ApiState>(ApiState.Empty)
    val news: StateFlow<ApiState> = _news

    private val _favoriteArticles = MutableStateFlow<List<Article>>(emptyList())
    val favoriteArticles: StateFlow<List<Article>> = _favoriteArticles

    init {
        onUpdateNews()
        getFavoriteArticlesFlow()
    }

    private fun getFavoriteArticlesFlow() {
        Log.i("HomeViewModel", "getFavoriteArticlesFlow")
        viewModelScope.launch {
            repository.getFavoriteArticlesFlow().collect {
                _favoriteArticles.value = it
            }
        }
    }

    private fun addArticle(article: Article) {
        Log.i("HomeViewModel", "Adding $article")
        viewModelScope.launch {
            repository.addArticle(article)
        }
    }

    private fun removeArticle(article: Article) {
        Log.i("HomeViewModel", "Removing $article")
        viewModelScope.launch {
            repository.removeArticle(article.toDaoModel())
        }
    }

    private fun onUpdateNews() {
        Log.i("HomeViewModel", "Updating news")

        viewModelScope.launch {
            _news.value = ApiState.Loading
            repository.topHeadlines(
                category = _category.value.lowercase(),
                country = _language.value.lowercase()
            ).catch { e ->
                _news.value = ApiState.Error(e)
            }.collect { data ->
                _news.value = ApiState.Success(data)
            }
        }
    }

    private fun onUpdateQuery(query: String) {
        _currentQuery.value = query
    }

    // TODO: store liked articles locally
    fun onLikeArticle(article: Article, toAdd: Boolean) {
        Log.i("HomeViewModel", "Liked, current: $toAdd $article")
        when(toAdd) {
            true -> addArticle(article)
            false -> removeArticle(article)
        }
    }

    fun onChangeLanguage(country: String) {
        _language.value = country
        onUpdateNews()
    }

    fun onChangeCategory(category: String) {
        _category.value = category
        onUpdateQuery(category)
        onUpdateNews()
    }

    fun onSearch(keyword: String) {
        onUpdateQuery("Search: $keyword")
        viewModelScope.launch {
            repository.searchByKeyword(keyword).collect {
                _news.value = ApiState.Success(it)
            }
        }
    }

    // TODO: Would be MUCH better to find by id instead
    fun isArticleLiked(article: Article) = favoriteArticles.value.contains(article)

}