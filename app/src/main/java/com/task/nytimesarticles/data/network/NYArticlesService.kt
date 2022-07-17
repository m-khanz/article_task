package com.task.nytimesarticles.data.network

import com.task.nytimesarticles.BuildConfig
import com.task.nytimesarticles.data.repository.ArticlesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NYArticlesService {

    //fetch NYTimes most popular articles...
    @GET("/svc/mostpopular/v2/mostviewed/{section}/{period}.json")
    suspend fun getArticles(
        @Path("period") period: Int = 7,
        @Path("section") section: String = "all-sections",
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): Response<ArticlesDTO>
}