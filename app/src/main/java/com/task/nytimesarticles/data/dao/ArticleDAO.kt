package  com.task.nytimesarticles.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.nytimesarticles.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)//replacing duplicate entries in table
    suspend fun addArticles(articles: List<Article>)

    @Query("DELETE FROM ${Article.TABLE_NAME}")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM ${Article.TABLE_NAME} WHERE ID = :id")//get article from table with unique id...
    fun getArticleById(id: Long): Flow<Article>

    @Query("SELECT * FROM ${Article.TABLE_NAME}")//get all the articles from db...
    fun getAllArticles(): Flow<List<Article>>
}
