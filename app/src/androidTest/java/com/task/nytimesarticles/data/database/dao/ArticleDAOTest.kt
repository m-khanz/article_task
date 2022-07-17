package com.task.nytimesarticles.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.nytimesarticles.data.dao.database.ArticlesDatabase
import com.task.nytimesarticles.model.Article
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleDAOTest {

    private lateinit var mDatabase: ArticlesDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        //this creates a database in memory
        mDatabase = Room.inMemoryDatabaseBuilder(context, ArticlesDatabase::class.java).build()
    }

    // test for the adding article to the db and if that record is present in db, then test case is passed
    @Test
    @Throws(InterruptedException::class)
    fun add_article_to_db() = runBlocking {
        val articles = listOf(
            Article(
                1,
                "Article-Url-1",
                "Article Title 1",
                "Test Author 1",
                description = "Test Description 1",
                publishedDate = "2022-17-07",
                url = "https//image1",
                coverImageUrl = "url:https://image-1"
            ),
            Article(
                2,
                "Article-Url-2",
                "Article Title 2",
                "Test Author 2",
                description = "Test Description 2",
                publishedDate = "2022-16-07",
                url = "https//image2",
                coverImageUrl = "url:https://image-2"
            )
        )

        mDatabase.getArticlesDao().addArticles(articles)
        val dbArticles = mDatabase.getArticlesDao().getAllArticles().first()
        assertThat(dbArticles, equalTo(articles))
    }

    @Test
    @Throws(InterruptedException::class)
    fun show_article_details_by_id() = runBlocking {
        val articles = listOf(
            Article(
                1,
                "Article-Url-1",
                "Article Title 1",
                "Test Author 1",
                description = "Test Description 1",
                publishedDate = "2022-17-07",
                url = "https//image1",
                coverImageUrl = "url:https://image-1"
            )
        )

        mDatabase.getArticlesDao().addArticles(articles)

        var dbArticle = mDatabase.getArticlesDao().getArticleById(1).first()
        assertThat(dbArticle, equalTo(articles[0]))
    }

    @After
    fun tearDown() {
        mDatabase.close()
    }
}
