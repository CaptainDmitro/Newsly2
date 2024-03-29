package org.captaindmitro.newsly2.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.captaindmitro.domain.entities.Article
import org.captaindmitro.domain.usecases.*
import org.captaindmitro.newsly2.utils.ApiState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val addArticleUseCase: AddArticleUseCase,
    private val removeArticleUseCase: RemoveArticleUseCase,
    private val getAllArticlesUseCase: GetAllArticlesUseCase,
    private val searchByKeywordUseCase: SearchByKeywordUseCase,
    private val writeCategoryUseCase: WriteCategoryUseCase,
    private val readCategoryUseCase: GetCategoryUseCase,
    private val writeLanguageUseCase: WriteLanguageUseCase,
    private val readLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()

        viewModelScope.launch {
            withContext(NonCancellable) {
                writeCategoryUseCase(_category.value)
                writeLanguageUseCase(_language.value)
            }
        }
    }

    private val _category: MutableStateFlow<String> = MutableStateFlow(readCategoryUseCase())
    val category: StateFlow<String> = _category.asStateFlow()

    private val _language: MutableStateFlow<String> = MutableStateFlow(readLanguageUseCase())
    val language: StateFlow<String> = _language.asStateFlow()

    private val _currentQuery: MutableStateFlow<String> = MutableStateFlow(category.value)
    val currentQuery = _currentQuery.asStateFlow()

    private val _news = MutableStateFlow<ApiState>(ApiState.Empty)
    val news: StateFlow<ApiState> = _news

    val favoriteArticles: StateFlow<List<Article>> = getAllArticlesUseCase().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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

    fun isArticleLiked(article: Article) = favoriteArticles.value.contains(article)
}