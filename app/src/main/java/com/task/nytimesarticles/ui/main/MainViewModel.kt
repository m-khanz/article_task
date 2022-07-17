package com.task.nytimesarticles.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.nytimesarticles.data.repository.ArticlesRepository
import com.task.nytimesarticles.model.Article
import com.task.nytimesarticles.model.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val articleRepository: ArticlesRepository) : ViewModel() {

    private val _articles: MutableStateFlow<States<List<Article>>> = MutableStateFlow(States.loading())

    val articles: StateFlow<States<List<Article>>> = _articles

    fun getArticles() {
        viewModelScope.launch {
            articleRepository.getArticlesList()
                .map { resource -> States.fromResource(resource) }
                .collect { state -> _articles.value = state }
        }
    }
}