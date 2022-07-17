package com.task.nytimesarticles.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.nytimesarticles.model.Article.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Article(
    @PrimaryKey
    val id: Long,
    val imageUrl: String?,
    val title: String?,
    val byline: String?,
    val description: String?,
    val publishedDate: String?,
    val url: String?,
    val coverImageUrl: String?

) {
    companion object {
        const val TABLE_NAME = "news_article"
    }
}
