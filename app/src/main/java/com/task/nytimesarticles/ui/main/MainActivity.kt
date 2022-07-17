package com.task.nytimesarticles.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.task.nytimesarticles.R
import com.task.nytimesarticles.databinding.ActivityMainBinding
import com.task.nytimesarticles.model.Article
import com.task.nytimesarticles.model.States
import com.task.nytimesarticles.ui.base.BaseActivity
import com.task.nytimesarticles.ui.details.ArticleDetailsActivity
import com.task.nytimesarticles.ui.main.adapter.ArticleListAdapter
import com.task.nytimesarticles.common.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel by viewModels()

    private val mAdapter = ArticleListAdapter(this::onItemClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) // Set AppTheme -> dark/light
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        initView()
        observePosts()
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    private fun initView() {
        mViewBinding.run {
            rvArticles.adapter = mAdapter

            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                //to get new articles...not implemented yet
                //getArticles()/
            }
        }
    }

    private fun observePosts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.articles.collect { state ->
                    when (state) {
                        is States.Loading -> showLoading(true)
                        is States.Success -> {
                            if (state.data.isNotEmpty()) {
                                mAdapter.submitList(state.data.toMutableList())
                                showLoading(false)
                            }
                        }
                        is States.Error -> {
                            Toast.makeText(applicationContext, state.message, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun getArticles() = mViewModel.getArticles()

    private fun showLoading(isLoading: Boolean) {
        mViewBinding.swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkResponse(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text = getString(R.string.no_internet_connection)
                mViewBinding.viewNetworkStatus.apply {
                    visibility = View.VISIBLE
                }
                getArticles()//this will get articles from local database...
            } else {
                if (mAdapter.itemCount == 0) getArticles()
                mViewBinding.textViewNetworkStatus.text = getString(R.string.connected)
                mViewBinding.viewNetworkStatus.apply {
                    animate()
                        .alpha(1f)
                        .setStartDelay(1000)
                        .setDuration(1000)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                visibility = View.GONE
                            }
                        })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_theme, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }

            else -> true
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private fun onItemClicked(post: Article, imageView: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )
        val postId = post.id ?: run {
            Toast.makeText(
                applicationContext,
                applicationContext.getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val intent = ArticleDetailsActivity.getStartIntent(this, postId)
        startActivity(intent, options.toBundle())
    }
}
