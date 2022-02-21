package com.example.newsly2.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsly2.model.Article
import com.example.newsly2.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _category = mutableStateOf("Business")
    val category: State<String> = _category

    private val _country = mutableStateOf("US")
    val country: State<String> = _country

    //    val news = repository.topBusinessHeadlines(category = _category.value.lowercase(), country = _country.value.lowercase())
//        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _news = MutableStateFlow<List<Article>>(emptyList())
    val news: StateFlow<List<Article>> = _news

    init {
        updateNews()
    }

    private fun updateNews() {
        viewModelScope.launch {
            repository.topHeadlines(
                category = _category.value.lowercase(),
                country = _country.value.lowercase()
            ).collect {
                _news.value = it
            }
        }
    }

    fun updateCountry(country: String) {
        _country.value = country
        updateNews()
    }

    fun updateCategory(category: String) {
        _category.value = category
        updateNews()
    }

}