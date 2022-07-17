package com.task.nytimesarticles.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import coil.load
import com.task.nytimesarticles.R
import com.task.nytimesarticles.databinding.ActivityArticleDetailsBinding
import com.task.nytimesarticles.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ArticleDetailsActivity : BaseActivity<ArticleDetailsViewModel, ActivityArticleDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: ArticleDetailsViewModel.ArticleDetailsViewModelFactory

    override val mViewModel: ArticleDetailsViewModel by viewModels {
        val articleId = intent.extras?.getLong(ARTICLE_ID_KEY)
            ?: throw IllegalArgumentException(applicationContext.getString(R.string.no_article_found))

        ArticleDetailsViewModel.provideFactory(viewModelFactory,  articleId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        initArticle()
    }

    private fun initArticle() {
        mViewModel.article.observe(this) { article ->
            mViewBinding.articleDetails.apply {
                articleTitle.text = article.title
                articleAuthor.text = article.byline
                articleBody.text = article.description
            }
            mViewBinding.ivArticle.load(article.imageUrl)
        }
    }

    override fun getViewBinding(): ActivityArticleDetailsBinding =
        ActivityArticleDetailsBinding.inflate(layoutInflater)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ARTICLE_ID_KEY = "article_id"

        fun getStartIntent(
            context: Context,
            articleId: Long
        ) = Intent(context, ArticleDetailsActivity::class.java).apply { putExtra(ARTICLE_ID_KEY, articleId) }
    }
}
