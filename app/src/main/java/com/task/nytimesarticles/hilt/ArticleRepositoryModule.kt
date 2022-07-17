package  com.task.nytimesarticles.hilt

import com.task.nytimesarticles.data.repository.DefaultArticleRepository
import com.task.nytimesarticles.data.repository.ArticlesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ArticleRepositoryModule {
    @ActivityRetainedScoped
    @Binds
    abstract fun bindPostRepository(repository: DefaultArticleRepository): ArticlesRepository
}
