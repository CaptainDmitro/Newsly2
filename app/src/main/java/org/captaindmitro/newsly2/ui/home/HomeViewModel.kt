package org.captaindmitro.newsly2.ui.home

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.domain.usecases.*
import org.captaindmitro.newsly2.utils.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val addArticleUseCase: AddArticleUseCase,
    private val removeArticleUseCase: RemoveArticleUseCase,
    private val getAllArticlesUseCase: GetAllArticlesUseCase,
    private val searchByKeywordUseCase: SearchByKeywordUseCase
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()

        with (sharedPreferences.edit()) {
            putString(LAST_VISITED_CATEGORY, category.value)
            putString(SELECTED_COUNTRY, language.value)
            apply()
        }
    }

    private val _category = mutableStateOf(sharedPreferences.getString(LAST_VISITED_CATEGORY, DEFAULT_CATEGORY)!!)
    val category: State<String> = _category

    private val _language = mutableStateOf(sharedPreferences.getString(SELECTED_COUNTRY, DEFAULT_COUNTRY)!!)
    private val language: State<String> = _language

    private val _currentQuery = mutableStateOf(category.value)
    val currentQuery: State<String> = _currentQuery

    private val _news = MutableStateFlow<ApiState>(ApiState.Empty)
    val news: StateFlow<ApiState> = _news

    val favoriteArticles: StateFlow<List<Article>> = getAllArticlesUseCase().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        updateNews()
    }

    private fun addArticleToFavorites(article: Article) {
        viewModelScope.launch {
            addArticleUseCase(article)
        }
    }

    private fun removeArticleFromFavorites(article: Article) {
        viewModelScope.launch {
            removeArticleUseCase(article)
        }
    }

    private fun updateNews() {
        viewModelScope.launch {
            _news.value = ApiState.Loading

            try {
                val data = getTopHeadlinesUseCase(
                    category = _category.value.lowercase(),
                    country = _language.value.lowercase()
                )
                _news.value = ApiState.Success(data)
            } catch (e: Exception) {
                _news.value = ApiState.Error(e)
            }
        }
    }

    private fun updateQuery(query: String) {
        _currentQuery.value = query
    }

    fun likeArticle(article: Article, toAdd: Boolean) {
        when(toAdd) {
            true -> addArticleToFavorites(article)
            false -> removeArticleFromFavorites(article)
        }
    }

    fun changeLanguage(country: String) {
        _language.value = country
        updateNews()
    }

    fun changeCategory(category: String) {
        _category.value = category
        updateQuery(category)
        updateNews()
    }

    fun searchQuery(keyword: String) {
        viewModelScope.launch {
            updateQuery("Search: $keyword")

            _news.value = ApiState.Loading

            try {
                val data = searchByKeywordUseCase(keyword)
                _news.value = ApiState.Success(data)
            } catch (e: Exception) {
                _news.value = ApiState.Error(e)
            }
        }
    }

    // TODO: Would be MUCH better to find by id instead
    fun isArticleLiked(article: Article) = favoriteArticles.value.contains(article)
}