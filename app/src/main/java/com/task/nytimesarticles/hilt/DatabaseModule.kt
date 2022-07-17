package  com.task.nytimesarticles.hilt
import android.app.Application
import com.task.nytimesarticles.data.dao.database.ArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(application: Application) = ArticlesDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providesDao(database: ArticlesDatabase) = database.getArticlesDao()
}
