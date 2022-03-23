package com.example.newsly2.utils

import org.captaindmitro.domain.model.Article

sealed class ApiState {
    object Loading : ApiState()
    class Error(val message: Throwable) : ApiState()
    class Success(val data: List<Article>) : ApiState()
    object Empty : ApiState()
}