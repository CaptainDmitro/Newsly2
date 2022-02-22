package com.example.newsly2.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsly2.model.Article
import com.example.newsly2.repository.Repository
import com.example.newsly2.utils.DEFAULT_CATEGORY
import com.example.newsly2.utils.DEFAULT_COUNTRY
import com.example.newsly2.utils.fakeArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _category = mutableStateOf(DEFAULT_CATEGORY)
    val category: State<String> = _category

    private val _country = mutableStateOf(DEFAULT_COUNTRY)
    val country: State<String> = _country

    private val _news = MutableStateFlow<List<Article>>(emptyList())
    val news: StateFlow<List<Article>> = _news

    // TODO: Change that awful implementation of detail view
    private val _detailsItem = mutableStateOf(fakeArticle)
    val detailsItem: State<Article> = _detailsItem

    private val _currentQuery = mutableStateOf(category.value)
    val currentQuery: State<String> = _currentQuery

    init {
        onUpdateNews()
    }

    private fun onUpdateNews() {
        viewModelScope.launch {
            repository.topHeadlines(
                category = _category.value.lowercase(),
                country = _country.value.lowercase()
            ).collect {
                _news.value = it
            }
        }
    }

    private fun updateCurrentQuery(query: String) {
        _currentQuery.value = query
    }

    fun onClickDetails(article: Article) {
        _detailsItem.value = article
    }

    fun onUpdateCountry(country: String) {
        _country.value = country
        onUpdateNews()
    }

    fun onUpdateCategory(category: String) {
        _category.value = category
        updateCurrentQuery(category)
        onUpdateNews()
    }

    fun onSearch(keyword: String) {
        updateCurrentQuery("Search: $keyword")
        viewModelScope.launch {
            repository.searchByKeyword(keyword).collect {
                _news.value = it
            }
        }
    }

}