package com.task.nytimesarticles.data.repository

import androidx.annotation.MainThread
import com.task.nytimesarticles.data.dao.ArticleDAO
import com.task.nytimesarticles.data.network.NYArticlesService
import com.task.nytimesarticles.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import retrofit2.Response
import javax.inject.Inject

/*
*repository to fetch remote data and storing it in database for offline mode...
*/
interface ArticlesRepository {
    fun getArticlesList(): Flow<Resource<List<Article>>>
    fun getArticleDetails(id: Long): Flow<Article>
}

class DefaultArticleRepository @Inject constructor(
    private val ArticleDAO: ArticleDAO,
    private val nyNewsService: NYArticlesService
) : ArticlesRepository {

    override fun getArticlesList(): Flow<Resource<List<Article>>> {
        return object : NetworkRepository<List<Article>, List<Article>>() {

            override suspend fun saveRemoteData(response: List<Article>) = ArticleDAO.addArticles(response)

            override fun fetchFromLocal(): Flow<List<Article>> = ArticleDAO.getAllArticles()//if no internet, then fetch from local db...

            override suspend fun fetchFromRemote(): Response<List<Article>> {
                var result = nyNewsService.getArticles()
                try {
                    return result?.let {
                        var data = mapArticlesDataItem(it)
                        return@let Response.success(data!!)
                    } as Response<List<Article>>
                } catch (e: Exception) {
                    return Response.error(result.code(), result.errorBody())
                }
            }
        }.asFlow()
    }

    fun mapArticlesDataItem(response: Response<ArticlesDTO>): List<Article>? {
        var articlesBO = response.body()
        var articles = articlesBO?.articlesList?.map {
            Article(
                it.id,
                if (!it.media.isNullOrEmpty()) it.media?.get(0)?.mediaMetaData?.get(2)?.url else "",
                it.title,
                it.byline,
                it.description,
                it.publishedDate,
                it.url,
                if (!it.media.isNullOrEmpty()) it.media?.get(0)?.mediaMetaData?.get(1)?.url else ""
            )
        }

        return articles
    }

    @MainThread
    override fun getArticleDetails(id: Long): Flow<Article> {

        return ArticleDAO.getArticleById(id).distinctUntilChanged()
    }
}
