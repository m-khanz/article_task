package com.task.nytimesarticles.ui.details


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.task.nytimesarticles.data.repository.ArticlesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ArticleDetailsViewModel @AssistedInject constructor(
    articleRepository: ArticlesRepository,
    @Assisted articleId: Long
) : ViewModel() {

    val article = articleRepository.getArticleDetails(articleId).asLiveData()

    @AssistedFactory
    interface ArticleDetailsViewModelFactory {
        fun create(articleId: Long): ArticleDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: ArticleDetailsViewModelFactory,
            articleId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(articleId) as T
            }
        }
    }
}
