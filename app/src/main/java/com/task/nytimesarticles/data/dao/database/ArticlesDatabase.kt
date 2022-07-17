package com.task.nytimesarticles.data.dao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.task.nytimesarticles.data.dao.ArticleDAO
import com.task.nytimesarticles.data.dao.migration.DatabaseMigrations
import com.task.nytimesarticles.model.Article

@Database(entities = [Article::class], version = DatabaseMigrations.DB_VERSION)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract fun getArticlesDao(): ArticleDAO

    companion object {
        private const val DATABASE_NAME = "db_articles"

        @Volatile//to broadcast initializing of instance to every thread...
        private var INSTANCE: ArticlesDatabase? = null

        fun getInstance(context: Context): ArticlesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {//synchronized for blocking the method if one method using it already...
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticlesDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
